@DisplayName("Тест обновления информации об альбоме")
@Test
@Order(14)
void testPutUpdateAlbum() {
//https://api.imgur.com/3/album/{{albumHash}}
String albumHash = "NAtkELv";
String albumTitle = "Новый заголовок";
String albumDescription = "Новое описание";
String albumPrivacy = "hidden";
String albumLayout = "grid";
String url = "album/" + albumHash;

given().when()
.spec(authSpec)
.log().all()
.formParam("title", albumTitle)
.formParam("description", albumDescription)
.formParam("privacy", albumPrivacy)
.formParam("layout", albumLayout)
.expect()
.log().all()
.spec(responseSpecCodeSuccessData)
.when()
.put(url);
}

@DisplayName("Album Favorite Test ")
@Test
@Order(15)
void testFavoriteAlbum() {
//https://api.imgur.com/3/album/{{albumHash}}/favorite
String albumHash = "XrrRp9";
String url = "album/" + albumHash + "/favorite";

given().when()
.spec(authSpec)
.expect()
.log().all()
.spec(responseSpecCodeSuccess)
.body("data", is("favorited"))
.when()
.post(url);
}

@DisplayName("Album removal test from favorites")
@Test
@Order(16)
void testUnfavoriteAlbum() {
//https://api.imgur.com/3/album/{{albumHash}}/favorite
String albumHash = "XrrRp9";
String url = "album/" + albumHash + "/favorite";

given().when()
.spec(authSpec)
.expect()
.log().all()
.spec(responseSpecCodeSuccess)
.body("data", is("unfavorited"))
.when()
.post(url);
}

@DisplayName("Test of adding images to album with Client_id by delete hashes of images")
@Test
@Order(17)
void testAddImagesToAnAlbumUnAuthed() {
//https://api.imgur.com/3/album/{{albumDeleteHash}}/add
//без токена выдаёт ошибку "You must own all the image deletehashes to add them to album"
String albumDeleteHash = "5D52u554BePrtSmApw";
String imageDeleteHash = "Eti4Dq60h9lZ5D52u55";
String imageDeleteHash2 = "cAzjAoNtNDA35D52u55";
String url = "album/" + albumDeleteHash + "/add";

given().when()
.spec(authSpec)
.log().all()
.formParam("deletehashes[]", imageDeleteHash)
.formParam("deletehashes[]", imageDeleteHash2)
.expect()
.log().all()
.spec(responseSpecCodeSuccess)
.when()
.post(url);
}

@DisplayName("Test for adding images to an album by image id")
@Test
@Order(18)
void testAddImagesToAnAlbumAuthed() {
// https://api.imgur.com/3/album/{{albumHash}}/add
String albumHash = "5D52u55";
String imageHash = "BGlPTjV";
String imageHash2 = "4NKkgq8";
String url = "album/" + albumHash + "/add";

given().when()
.spec(authSpec)
.log().all()
.formParam("ids[]", imageHash)
.formParam("ids[]", imageHash2)
.expect()
.log().all()
.spec(responseSpecCodeSuccess)
.when()
.post(url);
}

@DisplayName("Test for deleting images from an album with authorization")
@Test
@Order(19)
void testRemoveImagesFromAnAlbumAuthed() {
//https://api.imgur.com/3/album/{{albumHash}}/remove_images
String albumHash = "5D52u55";
String imageHash = "BGlPTjV";
String imageHash2 = "4NKkgq8";
String url = "album/" + albumHash + "/remove_images";

given().when()
.spec(authSpec)
.log().all()
.formParam("ids[]", imageHash)
.formParam("ids[]", imageHash2)
.expect()
.log().all()
.spec(responseSpecCodeSuccess)
.when()
.post(url);
}

