var lon;
var lat;
var layer;

var img64base;

var attribution = new ol.control.Attribution({
	collapsible: false
});


var map = new ol.Map({
	target: 'map',
	layers: [
		new ol.layer.Tile({
			source: new ol.source.OSM()
		})
	],
	view: new ol.View({
		center: ol.proj.fromLonLat([0, 0]),
		zoom: 2
	})
});


function readURL() {

	var filesSelected = document.getElementById("file").files;
	if (filesSelected.length > 0) {
		var fileToLoad = filesSelected[0];

		var fileReader = new FileReader();

		fileReader.onload = function(fileLoadedEvent) {
			var srcData = fileLoadedEvent.target.result; // <--- data: base64

			$('#myImg')
				.attr('src', srcData)
				.width(150)
				.height(200);

			img64base = srcData.split(',')[1]
		}

		fileReader.readAsDataURL(fileToLoad);
	}
}


$("#manifestation_form").submit(function(event) {

	// Stop form from submitting normally
	event.preventDefault();
	console.log("Registration...");

	let newImg = img64base;
	let name = $('input[name="name"]').val();
	let date = $('input[name="date"]').val();
	let type = document.getElementById('typeSelect').value; // 
	let numSeats = $('input[name="numSeats"]').val();
	let price = $('input[name="price"]').val();

	let street = $('input[name="street"]').val();
	let number = $('input[name="number"]').val();
	let city = $('input[name="city"]').val();
	let country = $('input[name="country"]').val();

	if (!newImg || !name || !date || !type || !numSeats || !price || !street || !number || !city || !country) {
		M.toast({ html: 'All fields must be filled!', classes: 'rounded', panning: 'center' });
		return;
	}

	var address = street + " " + number + " " + city + " " + country;
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
		url: "rest/manifestations/add",
		data: JSON.stringify({
			name: name,
			type: type,
			numSeats: numSeats,
			date: date,
			regularPrice: price,
			location: street + " " + number + ", " + city + ", " + country,
			lat: lat,
			lon: lon,
			poster: newImg
		}),
		contentType: 'application/json',
		success: function(result) {
			M.toast({ html: 'Successfully created a new manifestation', classes: 'rounded', panning: 'center' });
			window.location.href = "http://localhost:8080/RESTApp/index.html";

		},
		error: function() {
			M.toast({ html: 'Failed to create a new manifestation', classes: 'rounded', panning: 'center' });
		}
	});
});


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




$(document).ready(function() {
	var date = new Date();
	date.setDate(date.getDate() + 1);

	document.getElementById('date').min = date.toISOString().substr(0, 16)

	// SETUP MAP

	//var lat = parseFloat(manifestation.location.longitude)*1
	//var lon = parseFloat(manifestation.location.latitude)*1


});