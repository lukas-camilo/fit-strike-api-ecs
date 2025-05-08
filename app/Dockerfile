# Etapa 1: Build do binário nativo com GraalVM
FROM ghcr.io/graalvm/graalvm-community:24.0.1-ol8-20250415 as builder

WORKDIR /app

COPY . .

RUN rm -rf /root/.gradle /app/.gradle /app/build

RUN chmod +x ./gradlew

# Compile o binário nativo (ajuste conforme seu build tool)
RUN ./gradlew clean nativeCompile --no-daemon

# Etapa 2: Imagem final enxuta
FROM frolvlad/alpine-glibc:alpine-3.17

WORKDIR /app

# Copie o binário nativo gerado
COPY --from=builder /app/build/native/nativeCompile/fit-strike-api .

EXPOSE 8080

CMD ["./fit-strike-api"]