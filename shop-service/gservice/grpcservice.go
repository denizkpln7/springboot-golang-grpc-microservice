package gservice

import (
	"context"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"log"
	"shop-service/models"
	pb "shop-service/proto"
	"time"
)

var client pb.OrderServiceClient

type GService struct {
	grpc pb.OrderServiceClient
}

func NewGService() pb.OrderServiceClient {
	//grpc client
	conn, err := grpc.Dial("localhost:9091", grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatalf("Did not connect: %v", err)
	}

	defer conn.Close()

	client = pb.NewOrderServiceClient(conn)
	return client

}

func SaveGrpcProduct(client pb.OrderServiceClient) models.Product {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()

	res, err := client.SaveProduct(ctx, &pb.Product{
		Title:   "deneme",
		Balance: 15,
	})
	if err != nil {
		log.Fatalf("Could not greet: %v", err)
	}

	return models.Product{Title: res.Title, Balance: int(res.Balance)}
}
