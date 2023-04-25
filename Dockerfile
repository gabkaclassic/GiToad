FROM eclipse-temurin:19-jdk-jammy

WORKDIR .

COPY pom.xml ./
COPY src ./
COPY ./out/artifacts/GiToad_jar/GiToad.jar ./GiToad.jar

ENV CRYPTO_ALGORITHM_CIPHER=${CRYPTO_ALGORITHM_CIPHER}
ENV CRYPTO_ALGORITHM_KEY=${CRYPTO_ALGORITHM_KEY}
ENV CRYPTO_KEY=${CRYPTO_KEY}
ENV DB_NAME=${DB_NAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV DB_URL=${DB_URL}
ENV DB_USER=${DB_USER}
ENV JWT_EXPIRATION=${JWT_EXPIRATION}
ENV JWT_SECRET=${JWT_SECRET}
ENV SECRET_KEY=${SECRET_KEY}
ENV SPRING_SECURITY_PASSWORD=${SPRING_SECURITY_PASSWORD}
ENV SPRING_SECURITY_USERNAME=${SPRING_SECURITY_USERNAME}

EXPOSE 5001:5001

CMD ["java", "-jar", "./GiToad.jar"]
