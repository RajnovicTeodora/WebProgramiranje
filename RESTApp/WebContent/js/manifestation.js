let regular = 0;

function changed() {

	var select = document.getElementById('typeSelect')
	var numSeats = document.getElementById('numSeats')
	var total = document.getElementById('total')

	if (numSeats) {
		if (select.value == "REGULAR") {
			total.value = regular * numSeats.value;

		} else if (select.value == "VIP") {
			total.value = (regular * 4) * numSeats.value;

		} else {
			total.value = (regular * 2) * numSeats.value;

		}
	}
}

$("#reserve_form").submit(function(event) {


	event.preventDefault();
	console.log("Reserving ticket...");

	var select = document.getElementById('typeSelect')
	var numSeats = document.getElementById('numSeats').value
	var total = document.getElementById('total').value

	var answer = window.confirm("Confirm reservation of " + numSeats + " " + select.innerText + " ticket(s) for " + total + "?");
	if (answer) {
		const queryString = window.location.search;
		const urlParams = new URLSearchParams(queryString);
		$.ajax({
			type: 'POST',
			url: "rest/tickets/reserve",
			data: JSON.stringify({
				id: urlParams.get('manifestation'),
				numTickets: numSeats,
				ticketType: select.value,
				price: total

			}),
			contentType: 'application/json',
			success: function(result) {
				console.log(result);
				M.toast({ html: 'Successfully reserved ticket', classes: 'rounded', panning: 'center' });

			},
			error: function() {
				M.toast({ html: 'Failed to reserve ticket', classes: 'rounded', panning: 'center' });
			}
		});
	}
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

	$.get({
		url: 'rest/registration/registeredUser',
		success: function(result) {
			var user = result;
			var reserve = document.getElementById('reserve_form')
			var tickets = document.getElementById('numSeats')

			if (user) {
				if (manifestation.leftSeats == 0 || user.role != "USER" || manifestation.status != "ACTIVE")
					reserve.style.display = "none"
			}
			tickets.max = manifestation.leftSeats
			//document.getElementById('total').value = manifestation.regularPrice
		}
	});

	let basicInfo = $(
		'<div class="card horizontal">' +
		'<div class="card-image">' +
		'<img src="Resources/posters/ticket.jpg"">' +
		'</div>' +
		'<div class="card-content">' +
		'<span class="card-title">' + manifestation.name + ' - ' + manifestation.type + '</span>' +
		'<p> Date: ' + new Date(manifestation.date).toUTCString() + '</p>' +
		'<p>Price: ' + manifestation.regularPrice + '</p>' +
		'<p>Status: ' + manifestation.status + '</p> ' +
		'<p> Number of seats: ' + manifestation.numSeats + '</p>' +
		'<p> Available tickets: ' + manifestation.leftSeats + ' </p> ' +
		'<p style="padding-bottom:20px;"> Address: ' + manifestation.location.address + ' </p>' +
		'<a class="btn-floating right waves-effect waves-light teal darken-2" href="http://localhost:8080/RESTApp/editManifestation.html?manifestation=' + manifestation.id + '"><i class="material-icons">edit</i></a>' +
		'</div>' +
		'</div>');

	regular = manifestation.regularPrice
	document.getElementById('total').value = regular
	$('#manifestaion').append(basicInfo);

	var lat = parseFloat(manifestation.location.latitude) * 1
	var lon = parseFloat(manifestation.location.longitude) * 1
	//var lon = 4.35247
	//var lat = 50.84673

	var attribution = new ol.control.Attribution({
		collapsible: false
	});


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
			zoom: 10
		})
	});

	var layer = new ol.layer.Vector({
		source: new ol.source.Vector({
			features: [
				new ol.Feature({
					geometry: new ol.geom.Point(ol.proj.fromLonLat([lon, lat]))
				})
			]
		})
	});
	map.addLayer(layer);


	var layer = new ol.layer.Vector({
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
			console.log(manifestaton)
			showManifestation(manifestaton)
		}
	});
});