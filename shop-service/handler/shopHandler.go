package handler

import (
	"context"
	"github.com/gofiber/fiber/v2"
	"google.golang.org/grpc"
	"io"
	"log"
	//"shop-service/gservice"
	"shop-service/models"
	pb "shop-service/proto"
	"shop-service/repository"
	"strconv"
	"time"
)

type IShopHandler interface {
	Save(c *fiber.Ctx) error
	Get(c *fiber.Ctx) error
	GetById(c *fiber.Ctx) error
	SaveGrpcProduct(c *fiber.Ctx) error
	SaveGrpcProductsMaxBalance(c *fiber.Ctx) error
	SaveProductsDataAll(c *fiber.Ctx) error
	SaveProductDataAllBack(c *fiber.Ctx) error
}

type shopHandler struct {
	shopRepository repository.IShopRepository
}

func ShopHandlerInit(shopRepository repository.IShopRepository) IShopHandler {
	return &shopHandler{shopRepository}
}

func (u shopHandler) Save(c *fiber.Ctx) error {
	var req models.Product
	if err := c.BodyParser(&req); err != nil {
		return fiber.NewError(fiber.StatusServiceUnavailable, "Upss!")
	}
	if err := u.shopRepository.CreateUser(&req); err != nil {
		return fiber.NewError(fiber.StatusServiceUnavailable, "Kaydetmede bi sıkıntı oldu!")
	}

	return c.JSON(fiber.Map{
		"message": "Congratulation!, Your post is live",
	})
}

func (u shopHandler) Get(c *fiber.Ctx) error {
	products := u.shopRepository.GetAllProduct()
	return c.JSON(fiber.Map{
		"data": products,
	})
}

func (u shopHandler) GetById(c *fiber.Ctx) error {
	id, _ := strconv.Atoi(c.Params("id"))
	product := u.shopRepository.GetById(id)
	return c.JSON(fiber.Map{
		"data": product,
	})
}

func (u shopHandler) SaveGrpcProduct(c *fiber.Ctx) error {
	conn, err := grpc.Dial("localhost:9091", grpc.WithInsecure(), grpc.WithBlock())
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()

	client := pb.NewOrderServiceClient(conn)

	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()

	response, err := client.SaveProduct(ctx, &pb.Product{Title: "World", Balance: 15})
	if err != nil {
		log.Fatalf("could not greet: %v", err)
	}

	log.Printf("Greeting: %s", response.Title)

	return c.JSON(fiber.Map{
		"title":   response.Title,
		"balance": response.Balance,
		"id":      response.Id,
	})
}

func (u shopHandler) SaveGrpcProductsMaxBalance(c *fiber.Ctx) error {
	conn, err := grpc.Dial("localhost:9091", grpc.WithInsecure(), grpc.WithBlock())
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()

	client := pb.NewOrderServiceClient(conn)
	stream, err := client.SaveProductsData(context.Background())

	var list []pb.Product
	for i := 0; i < 3; i++ {
		list = append(list, pb.Product{Title: "deneme" + strconv.Itoa(i), Balance: int32(i)})
	}

	for _, product := range list {
		if err := stream.Send(&product); err != nil {
			log.Fatalf("Error while sending %v", err)
		}
	}

	res, err := stream.CloseAndRecv()
	log.Printf("Client Streaming finished")
	if err != nil {
		log.Fatalf("Error while receiving %v", err)
	}
	log.Printf("email %v surname %v", res.Balance, res.Title)

	return c.JSON(fiber.Map{
		"title":   res.Title,
		"balance": res.Balance,
		"id":      res.Id,
	})

}

func (u shopHandler) SaveProductsDataAll(c *fiber.Ctx) error {
	conn, err := grpc.Dial("localhost:9091", grpc.WithInsecure(), grpc.WithBlock())
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()

	client := pb.NewOrderServiceClient(conn)

	product := &pb.Product{
		Title:   "deneme1",
		Balance: 15,
	}

	stream, err := client.SaveProductsDataAll(context.Background(), product)

	if err != nil {
		log.Fatalf("Could not send names: %v", err)
	}

	var list []pb.SaveProduct

	for {
		message, err := stream.Recv()
		if err == io.EOF {
			break
		}
		if err != nil {
			log.Fatalf("Error while streaming %v", err)
		}
		list = append(list, pb.SaveProduct{Balance: message.Balance, Title: message.Title, Id: message.Id})
	}

	return c.JSON(fiber.Map{
		"list": list,
	})

}

func (u shopHandler) SaveProductDataAllBack(c *fiber.Ctx) error {
	conn, err := grpc.Dial("localhost:9091", grpc.WithInsecure(), grpc.WithBlock())
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()

	client := pb.NewOrderServiceClient(conn)

	stream, err := client.SaveProductDataAllBack(context.Background())
	if err != nil {
		log.Fatalf("Could not send names: %v", err)
	}

	waitc := make(chan struct{})

	var list []pb.SaveProduct

	go func() {
		for {
			message, err := stream.Recv()
			if err == io.EOF {
				break
			}
			if err != nil {
				log.Fatalf("Error while streaming %v", err)
			}
			list = append(list, pb.SaveProduct{Balance: message.Balance, Title: message.Title, Id: message.Id})
		}
		close(waitc)
	}()

	for i := 0; i < 5; i++ {
		req := &pb.Product{
			Title:   "admin" + strconv.Itoa(i),
			Balance: int32(i),
		}
		if err := stream.Send(req); err != nil {
			log.Fatalf("Error while sending %v", err)
		}
	}

	stream.CloseSend()
	<-waitc
	log.Printf("Bidirectional Streaming finished")

	return c.JSON(fiber.Map{
		"list": list,
	})

}
