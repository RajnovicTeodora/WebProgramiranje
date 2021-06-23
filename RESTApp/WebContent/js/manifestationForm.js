var lon;
var lat;
var layer;

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

$("#manifestation_form").submit(function(event) {

	// Stop form from submitting normally
	event.preventDefault();
	console.log("Registration...");

	let file = $('input[name="file"]').val();
	let name = $('input[name="name"]').val();
	let date = $('input[name="date"]').val();
	let type = document.getElementById('typeSelect').value; // 
	let numSeats = $('input[name="numSeats"]').val();
	let price = $('input[name="price"]').val();

	let street = $('input[name="street"]').val();
	let number = $('input[name="number"]').val();
	let city = $('input[name="city"]').val();
	let country = $('input[name="country"]').val();

	if (!file || !name || !date || !type || !numSeats || !price || !street || !number || !city || !country) {
		$('#error').text('All fields must be filled!');
		$("#error").show().delay(3000).fadeOut();
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
			poster: file
		}),
		contentType: 'application/json',
		success: function(result) {
			console.log(result);
			M.toast({ html: 'Successfully created a new manifestation', classes: 'rounded', panning: 'center' });

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