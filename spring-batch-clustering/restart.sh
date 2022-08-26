docker kill manager;
docker rm manager;
docker kill worker-1;
docker rm worker-1;
docker kill worker-2;
docker rm worker-2;
docker kill worker-3;
docker rm worker-3;
docker-compose build;
docker-compose up -d
