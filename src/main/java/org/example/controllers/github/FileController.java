package org.example.controllers.github;

import lombok.RequiredArgsConstructor;
import org.example.controllers.responses.FileResponse;
import org.example.github.utils.GithubUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("files")
public class FileController {

    private final GithubUtils githubUtils;

    @GetMapping
    public FileResponse getFile(@RequestParam String repositoryName,
                                @RequestParam String path) throws IOException {

        return githubUtils.getFilesUtils().getFile(repositoryName, path);
    }

    @DeleteMapping
    public FileResponse deleteFile(@RequestParam String repositoryName,
                                   @RequestParam String path,
                                   @RequestParam(required = false) String message) throws IOException {

        return githubUtils.getFilesUtils().deleteFile(repositoryName, path, message);
    }

    @PutMapping
    public FileResponse updateFile(@RequestParam String repositoryName,
                                   @RequestParam String path,
                                   @RequestParam String content,
                                   @RequestParam(required = false) String message) throws IOException {

        return githubUtils.getFilesUtils().updateFile(repositoryName, path, content, message);
    }
}