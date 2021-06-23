
var registration_url = ".../services/rest/registration/add";

var lon = 4.35247
var lat = 50.84673

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
		center: ol.proj.fromLonLat([lon, lat]),
		zoom: 4
	})
});

var layer;

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

	let username = $('input[name="username"]').val();
	let password = $('input[name="password"]').val();
	let name = $('input[name="name"]').val();
	let surname = $('input[name="surname"]').val();
	let gender = document.querySelector('input[name="gender"]:checked').value;
	let birthday = $('input[name="date"]').val();

	if (!username || !password || !name || !surname || !gender || !birthday) {
		$('#error').text('All fields must be filled!');
		$("#error").show().delay(3000).fadeOut();
		return;
	}

	var today = new Date().toISOString().split("T")[0];;
	if (birthday >= today) {
		$('#error').text('Date of birth must be before today\'s date');
		$("#error").show().delay(3000).fadeOut();
		return;
	}
	$.ajax({
		type: 'POST',
		url: "rest/registration/add",
		data: JSON.stringify({
			username: username,
			password: password,
			name: name,
			surname: surname,
			gender: gender,
			birthday: birthday

		}),
		contentType: 'application/json',
		success: function(result) {
			console.log(result);
			M.toast({ html: 'Successfully registered', classes: 'rounded', panning: 'center' });

		},
		error: function() {
			$('#error').text("Invalid input!");
			$("#error").show().delay(2000).fadeOut();
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