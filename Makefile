decrypt:
	sops -d -i src/main/resources/application.yaml

encrypt:
	sops -e -i src/main/resources/application.yaml