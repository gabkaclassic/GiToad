package org.example.utils.github;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.request_processing.responses.LogicalStateResponse;
import org.example.request_processing.responses.MySelfResponse;
import org.example.request_processing.responses.RegistrationResponse;
import org.example.utils.crypt.Cryptographer;
import org.example.data.accounts.AccountService;
import org.example.utils.jwt.JwtUtil;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AccountUtils {
    private GitHub client;

    private final AccountService accountService;

    private final Cryptographer cryptographer;

    private final JwtUtil jwtUtil;

    @SneakyThrows
    GitHub loginByJwt(byte[] jwt) {

        return client = new GitHubBuilder()
                .withPassword(decrypt(jwt), decrypt(jwt)).build();

    }

    private boolean checkByJwt(String jwt) {
        try {
            new GitHubBuilder()
                    .withJwtToken(jwt)
                    .build();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private byte[] encrypt(String value) {
        return cryptographer.encrypt(value.getBytes());
    }
    private String decrypt(byte[] value) {
        return new String(cryptographer.decrypt(value));
    }

    public ResponseEntity<RegistrationResponse> updateData(String jwt, String token) {

        var violations = new ArrayList<String>();

        validateJwt(token, violations);

        if(!violations.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegistrationResponse(violations));

        var id = jwtUtil.extractId(jwt);

        try {
            accountService.updateData(
                    id,
                    encrypt(token)
            );
        } catch (Exception e) {
            e.printStackTrace();
            violations.add("Registration error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegistrationResponse(violations));
        }

        return ResponseEntity.ok().body(new RegistrationResponse(violations));
    }

    private void validateJwt(String jwt, List<String> violations) {

        if(jwt != null && !jwt.isEmpty() && !checkByJwt(jwt))
            violations.add("Invalid JWT");
    }

    SetupData setup(String id) {

        byte[] jwt = new byte[0];
        try {
            jwt = accountService.findJwtById(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        }


        client = loginByJwt(jwt);

        return new SetupData(jwt, client);
    }

    public ResponseEntity<LogicalStateResponse> exists(String token) {

        return ResponseEntity.ok(new LogicalStateResponse(accountService.existsById(jwtUtil.extractId(token))));
    }

    public ResponseEntity<MySelfResponse> getMyself() throws IOException {

        var myself = client.getMyself();
        var response = new MySelfResponse(myself.getName(), myself.getLogin(), myself.getAvatarUrl(), myself.getBio(), myself.getUrl().toString());

        return ResponseEntity.ok(response);
    }
}