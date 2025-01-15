data class Shipping(
    val store_pick_up: Boolean,
    val free_shipping: Boolean,
    val logistic_type: String,
    val mode: String,
    val tags: List<String>,
    val benefits: String?,
    val promise: String?,
    val shipping_score: Number
)

data class Seller(
    val id: String,
    val nickname: String
)

data class Address(
    val state_id: String,
    val state_name: String,
    val city_id: String,
    val city_name: String
)

data class AttributeValue(
    val id: String?,
    val name: String,
    val struct: Map<String, Any>?,
    val source: String
)

data class Attribute(
    val id: String,
    val name: String,
    val value_id: String?,
    val value_name: String,
    val attribute_group_id: String,
    val attribute_group_name: String,
    val value_struct: Map<String, Any>?,
    val values: List<AttributeValue>,
    val source: String,
    val value_type: String
)

data class SalePrice(
    val price_id: String,
    val amount: Number,
    val conditions: Map<String, Any>,
    val currency_id: String,
    val exchange_rate: Number?,
    val payment_method_prices: List<Map<String, Any>>,
    val payment_method_type: String,
    val regular_amount: Number,
    val type: String,
    val metadata: Map<String, Any>
)

data class Installments(
    val quantity: Number,
    val amount: Number,
    val rate: Number,
    val currency_id: String,
    val metadata: Map<String, Any>
)

data class Results(
    val id: String,
    val title: String,
    val condition: String,
    val thumbnail_id: String,
    val catalog_product_id: String,
    val listing_type_id: String,
    val sanitized_title: String,
    val permalink: String,
    val buying_mode: String,
    val site_id: String,
    val category_id: String,
    val domain_id: String,
    val thumbnail: String,
    val currency_id: String,
    val order_backend: Number,
    val price: Number,
    val original_price: Number,
    val sale_price: SalePrice,
    val available_quantity: Number,
    val official_store_id: String?,
    val use_thumbnail_id: Boolean,
    val accepts_mercadopago: Boolean,
    val shipping: Shipping,
    val stop_time: String,
    val seller: Seller,
    val address: Address,
    val attributes: List<Attribute>,
    val installments: Installments,
    val winner_item_id: String?,
    val catalog_listing: Boolean,
    val discounts: String?,
    val promotion_decorations: String?,
    val promotions: String?,
    val inventory_id: String?
)

data class ProductoModel(
    val results: List<Results>
)
