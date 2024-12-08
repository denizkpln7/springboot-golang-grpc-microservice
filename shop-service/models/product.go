package models

import "gorm.io/gorm"

type Product struct {
	gorm.Model
	Title         string `json:"title"`
	Balance       int    `json:"balance"`
	ProductNumber string `json:"productNumber"`
}
