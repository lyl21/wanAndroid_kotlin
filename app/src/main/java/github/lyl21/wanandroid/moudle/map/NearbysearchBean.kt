package github.lyl21.wanandroid.moudle.map


class NearBySearchBean(
    val html_attributions: List<String>,
    val next_page_token: String,
    val results: List<NearBySearchBeanDes>,
    val status: String
)

class NearBySearchBeanDes(
    val business_status: String,
    val geometry: Geometry,

    )

class Geometry(
    val location: Location,
    val viewport: Viewport,
    icon: String,
    icon_background_color: String,
    icon_mask_base_uri: String,
    name: String,
    opening_hours: OpeningHours,
    photos: List<Photo>,
    place_id:String,
    plus_code:PlusCode,
    price_level:Int,
    rating:Double,
    reference:String,
    scope:String,
    types:List<String>,
    user_ratings_total:Int,
    vicinity:String,
    )

class Location(
    val lat: String,
    val lng: String
)

class Viewport(
    val northeast: Location,
    val southwest: Location,
)

class OpeningHours(
    val open_now: Boolean,
)

class Photo(
    val height: Int,
    var html_attributions: List<String>,
    val photo_reference: String,
    val width: String
)

class PlusCode(
        val compound_code:String,
        val global_code:String,
)

