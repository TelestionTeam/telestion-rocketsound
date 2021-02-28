# RocketSound

## Installation

### Published Release

1. Login to docker with your github credentials or an access token: `docker login docker.pkg.github.com`
2. Run image with: `docker run -p 9870:9870 docker.pkg.github.com/telestionteam/rocketsound/telestion-rocketsound:latest`

### From Source

1. Clone the repository
2. Build project with: `./gradlew build`
3. Build docker image with: `docker build -t telestion-rocketsound`
4. Run docker image with: `docker run -p 9870:9870 telestion-rocketsound`
