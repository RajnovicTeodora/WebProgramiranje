//var file = document.getElementById('file');
var manifestationName = document.getElementById('name');
var type = document.getElementById('typeSelect');
var numSeats = document.getElementById('numSeats');
var price = document.getElementById('price');
var street = document.getElementById('street');
var number = document.getElementById('number');
var city = document.getElementById('city');
var country = document.getElementById('country');
var date = document.getElementById('date');

var lon;
var lat;

var layer;

var attribution = new ol.control.Attribution({
	collapsible: false
});


var map;

var queryString = window.location.search;
var urlParams = new URLSearchParams(queryString);
var id = urlParams.get('manifestation')

function changed() {

	// Stop form from submitting normally
	console.log("Finding address...");

	let street = $('input[name="street"]').val();
	let number = $('input[name="number"]').val();
	let city = $('input[name="city"]').val();
	let country = $('input[name="country"]').val();

	if (!street || !number || !city || !country) {
		return;
	}

	var address = street + " " + number + " " + city + " " + country;
	$.ajax({
		type: 'GET',
		url: 'https://nominatim.openstreetmap.org/search/' + encodeURIComponent(address) + '?format=json&addressdetails=1&limit=1&polygon_svg=1',
		contentType: 'application/json',
		success: function(result) {
			if (result.length <= 0)
				return;
			lon = result[0].lon * 1
			lat = result[0].lat * 1

			if (layer)
				map.removeLayer(layer)
			layer = new ol.layer.Vector({
				source: new ol.source.Vector({
					features: [
						new ol.Feature({
							geometry: new ol.geom.Point(ol.proj.fromLonLat([lon, lat]))
						})
					]
				})
			});
			map.addLayer(layer);
			M.toast({ html: 'Address found', classes: 'rounded', panning: 'center' });
		}
	});
}

$("#manifestation_form").submit(function(event) {

	// Stop form from submitting normally
	event.preventDefault();
	console.log("Editing manifestation...");

	let newFile = $('input[name="file"]').val();
	let newName = $('input[name="name"]').val();
	let newType = document.getElementById('typeSelect').value; // 
	let newStreet = $('input[name="street"]').val();
	let newNumber = $('input[name="number"]').val();
	let newCity = $('input[name="city"]').val();
	let newCountry = $('input[name="country"]').val();

	if (!newFile || !newName || !newType || !newStreet || !newNumber || !newCity || !newCountry) {
		$('#error').text('All fields must be filled!');
		$("#error").show().delay(3000).fadeOut();
		return;
	}

	var address = newStreet + " " + newNumber + " " + newCity + " " + newCountry;
	$.ajax({
		type: 'GET',
		url: 'https://nominatim.openstreetmap.org/search/' + encodeURIComponent(address) + '?format=json&addressdetails=1&limit=1&polygon_svg=1',
		contentType: 'application/json',
		success: function(result) {
			if (result.length <= 0) {
				M.toast({ html: 'Address not found', classes: 'rounded', panning: 'center' });
				return;
			}
			lon = result[0].lon * 1
			lat = result[0].lat * 1
		}
	});

	$.ajax({
		type: 'POST',
		url: "rest/manifestations/edit",
		data: JSON.stringify({
			id: id,
			name: newName,
			type: newType,
			location: newStreet + " " + newNumber + ", " + newCity + ", " + newCountry,
			lat: lat,
			lon: lon,
			poster: "ugh"
		}),
		contentType: 'application/json',
		success: function(result) {
			console.log(result);
			M.toast({ html: 'Successfully edited the manifestation', classes: 'rounded', panning: 'center' });
			window.location.href = "http://localhost:8080/RESTApp/vendorManifestations.html";

		},
		error: function() {
			M.toast({ html: 'Failed to edit the manifestation', classes: 'rounded', panning: 'center' });
		}
	});
});




function readURL(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();

		reader.onload = function(e) {
			$('#blah')
				.attr('src', e.target.result)
				.width(150)
				.height(200);
		};

		reader.readAsDataURL(input.files[0]);

	}
}


function showManifestation(manifestation) {
	//file.value = manifestaton.poster // TODO
	manifestationName.value = manifestation.name
	type.value = manifestation.type
	numSeats.value = manifestation.numSeats;

	var address = manifestation.location.address.split(',');
	var temp = address[0].split(' ');
	var i = 0;
	var streetTemp = "";
	while (i < temp.length - 1) {
		streetTemp += temp[i] + " "
		i++
	}
	street.value = streetTemp
	number.value = temp[temp.length - 1];
	city.value = address[1];
	country.value = address[2];
	price.value = manifestation.regularPrice;
	date.value = new Date(manifestation.date).toISOString().substring(0, 19);

	lon = manifestation.location.longitude * 1
	lat = manifestation.location.latitude * 1

	map = new ol.Map({
		target: 'map',
		layers: [
			new ol.layer.Tile({
				source: new ol.source.OSM()
			})
		],
		view: new ol.View({
			center: ol.proj.fromLonLat([lon, lat]),
			zoom: 5
		})
	});

	layer = new ol.layer.Vector({
		source: new ol.source.Vector({
			features: [
				new ol.Feature({
					geometry: new ol.geom.Point(ol.proj.fromLonLat([lon, lat]))
				})
			]
		})
	});
	map.addLayer(layer);


}

$(document).ready(function() {
	const queryString = window.location.search;
	const urlParams = new URLSearchParams(queryString);
	const id = urlParams.get('manifestation')

	$.get({
		url: 'rest/manifestations/' + id,
		success: function(manifestaton) {

			showManifestation(manifestaton)
		}
	});

});