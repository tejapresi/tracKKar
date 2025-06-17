# Use platform-aware shell and Gradle command
ifeq ($(OS),Windows_NT)
    SHELL := cmd.exe
    GRADLE_CMD := gradlew.bat
else
    SHELL := /bin/bash
    GRADLE_CMD := ./gradlew
endif

.PHONY: up up-mac down logs

up:
	$(GRADLE_CMD) clean build
	docker-compose -f docker-compose.yml build
	docker-compose -f docker-compose.yml up -d

down:
	docker-compose -f docker-compose.yml down

logs:
	docker logs -f gatestatus-backend
