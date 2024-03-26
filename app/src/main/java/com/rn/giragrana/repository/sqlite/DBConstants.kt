package com.rn.giragrana.repository.sqlite

const val DATABASE_NAME = "dbGiragrana"
const val DATABASE_VERSION = 1

const val TABLE_PRODUCT = "product"
const val PRODUCT_COLUMN_ID = "_id"
const val PRODUCT_COLUMN_NAME = "name"
const val PRODUCT_COLUMN_DESCRIPTION = "description"
const val PRODUCT_COLUMN_PRICE = "price"
const val PRODUCT_COLUMN_SOLD = "sold"

const val TABLE_CLIENT = "client"
const val CLIENT_COLUMN_ID = "_id"
const val CLIENT_COLUMN_NAME = "name"
const val CLIENT_COLUMN_CONTACT = "contact"

const val TABLE_RESALE = "resale"
const val RESALE_COLUMN_ID = "_id"
const val RESALE_COLUMN_PRICE = "price"
const val RESALE_COLUMN_RESALE_PRICE = "resalePrice"
const val RESALE_COLUMN_DATE = "resaleDate"
const val RESALE_COLUMN_RECEIVING = "receivingDate"
const val RESALE_COLUMN_PAYMENT_METHOD = "paymentMethod"

const val RESALE_COLUMN_PRODUCT_ID = "productId"
const val RESALE_COLUMN_CLIENT_ID = "clientId"
