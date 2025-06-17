SHELL := cmd.exe

up:
	gradlew.bat build
	docker-compose -f docker-compose.yml build
	docker-compose -f docker-compose.yml up -d

down:
	docker-compose -f docker-compose.yml down

logs:
	docker logs -f gatestatus-backend

