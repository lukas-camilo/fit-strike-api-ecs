# Etapa 1: Build da imagem nativa com GraalVM
FROM ghcr.io/graalvm/graalvm-community:24.0.1-ol8-20250415 as builder

WORKDIR /app

# Copie todos os arquivos do projeto (inclua build scripts)
COPY . .

# Dê permissão de execução ao gradlew (se usar Gradle Wrapper)
RUN chmod +x ./gradlew

# Compile o binário nativo
RUN ./gradlew bootBuildImage --no-daemon

# Etapa 2: Imagem final enxuta
FROM frolvlad/alpine-glibc:alpine-3.17

WORKDIR /app

# Copie o binário nativo gerado
COPY --from=builder /app/build/native/nativeCompile/fit-strike-api .

EXPOSE 8080

CMD ["./fit-strike-api"]