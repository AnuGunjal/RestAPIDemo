import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseOptions;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.*; // we have to import manually by placing static and it will remove the error for given, when, then methods/keywords

import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.payload;

public class Basics {

	private static ValidatableResponse statusCode;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//validate if Add place API is working as expected
		
		//rest assured works on three principals
		//Given --> all the input details to submit the API
		//when --> Submit the API  -- resource, http method
		//Then -->Validate the response

		RestAssured.baseURI ="https://rahulshettyacademy.com";
		
		
	
	
			
			
			String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(payload.AddPlace()).when().post("maps/api/place/add/json").then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("server","Apache/2.4.52 (Ubuntu)").extract().response().asString();
			
	
			System.out.println(response);
			
			JsonPath js = new JsonPath(response); // for parsing JSON
			//this class takes input as string and convert the string /parse into JSON
			String placeId =js.getString("place_id");
			
			System.out.println(placeId);  // got the placeid
			
			
		// requirement to add a place and update it with new address --> then we have to use put place API
		// Add place --> update place with New address --> Get place to validate if new address is present in response
	
		
			
			//after getting the place id next step is to update the place with place id
			String newAddress ="70 winter walk, USA";
			
			given().log().all().queryParam("key","qaclick123").header("Content-Type", "application/json")
			.body("{\r\n"
					+ "\"place_id\":\""+placeId+"\",\r\n"
					+ "\"address\":\""+newAddress+"\",\r\n"
					+ "\"key\":\"qaclick123\"\r\n"
					+ "}")
			.when().put("maps/api/place/update/json")
			.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
			
			
			//post update, we need to do the get place API
			
			
		String getPlaceResponse=	given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
			.when().get("maps/api/place/get/json")
			.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1 = new JsonPath(getPlaceResponse);
		String ActualAddress = js1.getString("address");
		System.out.println(ActualAddress);
		
		
		// rest assured doesn't have inbuilt to validate the updated address and actual address
		//hence , here will have to user testing framework annotation such as cucumber/ junit/testng
		
		Assert.assertEquals(newAddress, ActualAddress); // used testng annotation to validate the expected and actual address 
		
	}	
}
