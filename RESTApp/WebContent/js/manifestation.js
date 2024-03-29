let regular = 0;

$("#comment_form").submit(function(event) {
	event.preventDefault();
	console.log("Commenting...");

	var select = document.getElementById('rating')
	var text = document.getElementById('comment').value

	if (!text)
		return

	const queryString = window.location.search;
	const urlParams = new URLSearchParams(queryString);
	$.ajax({
		type: 'POST',
		url: "rest/comments/add",
		data: JSON.stringify({
			manifestation: urlParams.get('manifestation'),
			rating: select.value,
			text: text

		}),
		contentType: 'application/json',
		success: function() {
			M.toast({ html: 'Successfully made a comment. It will be visible if a vendor approves it.', classes: 'rounded', panning: 'center' });

		},
		error: function() {
			M.toast({ html: 'Failed to create a comment', classes: 'rounded', panning: 'center' });
		}
	});
});

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

var discount;

$("#reserve_form").submit(function(event) {

	event.preventDefault();
	console.log("Reserving ticket...");

	var select = document.getElementById('typeSelect')
	var numSeats = document.getElementById('numSeats').value
	var total = document.getElementById('total').value
	var dicounted = total
	if (discount && discount > 0) {
		dicounted = total - total * discount / 100
	}

	var answer = window.confirm("Confirm reservation of " + numSeats + " " + select.innerText + " ticket(s) for a (discounted) price of " + dicounted + "$?");
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
				window.location.href = "http://localhost:8080/RESTApp/manifestation.html?manifestation=" + urlParams.get('manifestation');
				M.toast({ html: 'Successfully reserved ticket', classes: 'rounded', panning: 'center' });

			},
			error: function() {
				M.toast({ html: 'Failed to reserve ticket', classes: 'rounded', panning: 'center' });
			}
		});
	}
});

function showComment(comment, user) {

	if (user == null || user.role == "USER") {
		let item = $('<li class="collection-item" id="' + comment.id + '"><div>' +
			'<span class="title">' + comment.user + '</span>' +
			'<p>Rating: ' + comment.rating + '<br>Comment: ' + comment.text + ' </p>' +
			'</div></li>');
		$('#comment_list').append(item);
	}
	else if (user.role == "VENDOR") {
		if (comment.status == "WAITING") {
			let item = $('<li class="collection-item" id="' + comment.id + '"><div>' +
				'<span class="title">' + comment.user + " - " + comment.status + '</span>' +
				'<p>Rating: ' + comment.rating + '<br>Comment: ' + comment.text + ' <button onclick="approve(' + comment.id + ')" class="secondary-content btn-floating btn-small waves-effect waves-light"><i class="material-icons">check_circle</i></button><button onclick="reject(' + comment.id + ')" class="secondary-content  btn-floating btn-small waves-effect waves-light"><i class="material-icons">cancel</i></button><br></p>' +
				'</div></li>');
			$('#comment_list').append(item);
		} else {
			let item = $('<li class="collection-item" id="' + comment.id + '"><div>' +
				'<span class="title">' + comment.user + " - " + comment.status + '</span>' +
				'<p>Rating: ' + comment.rating + '<br>Comment: ' + comment.text + '</p>' +
				'</div></li>');
			$('#comment_list').append(item);
		}

	}
	else if (user.role == "ADMINISTRATOR") {

		let item = $('<li class="collection-item" id="' + comment.id + '"><div>' +
			'<span class="title">' + comment.user + " - " + comment.status + '</span>' +
			'<p>Rating: ' + comment.rating + '<br>Comment: ' + comment.text + ' <button onclick="deleteComment(' + comment.id + ')" class="secondary-content btn-floating btn-small waves-effect waves-light"><i class="material-icons">delete</i></button></p>' +
			'</div></li>');
		$('#comment_list').append(item);

	} else {
		return
	}
}

function deleteComment(id) {
	$.ajax({
		type: 'GET',
		contentType: 'application/json',
		url: 'rest/comments/delete/' + id,
		success: function(comment) {

			const listItem = document.getElementById(comment.id);
			listItem.innerHTML = ''
			M.toast({ html: 'Successfully deleted user comment', classes: 'rounded', panning: 'center' });
		},
		error: function() {
			M.toast({ html: 'Unable to deleted user comment', classes: 'rounded', panning: 'center' });
		}
	});
}

function approve(id) {
	$.ajax({
		type: 'GET',
		contentType: 'application/json',
		url: 'rest/comments/approve/' + id,
		success: function(comment) {
			let item = '<div>' +
				'<span class="title">' + comment.user + " - " + comment.status + '</span>' +
				'<p>Rating: ' + comment.rating + '<br>Comment: ' + comment.text + '</p>' +
				'</div>';
			const listItem = document.getElementById(comment.id);
			listItem.innerHTML = item
			M.toast({ html: 'Successfully approved user comment', classes: 'rounded', panning: 'center' });
		},
		error: function() {
			M.toast({ html: 'Unable to approve user comment', classes: 'rounded', panning: 'center' });
		}
	});
}

function reject(id) {
	$.ajax({
		type: 'GET',
		contentType: 'application/json',
		url: 'rest/comments/reject/' + id,
		success: function(comment) {
			let item = '<div>' +
				'<span class="title">' + comment.user + " - " + comment.status + '</span>' +
				'<p>Rating: ' + comment.rating + '<br>Comment: ' + comment.text + '</p>' +
				'</div>';
			const listItem = document.getElementById(comment.id);
			listItem.innerHTML = item
			M.toast({ html: 'Successfully rejected user comment', classes: 'rounded', panning: 'center' });
		},
		error: function() {
			M.toast({ html: 'Unable to reject user comment', classes: 'rounded', panning: 'center' });
		}
	});
}


function showManifestation(manifestation, user) {

	var type = manifestation.type.charAt(0) + manifestation.type.toLowerCase().slice(1)
	var status = manifestation.status.charAt(0) + manifestation.status.toLowerCase().slice(1)

	var reserve = document.getElementById('reserve_form')
	var tickets = document.getElementById('numSeats')

	var date = new Date(manifestation.date)
	var today = new Date()

	// Ticket reservation
	if (user == null || manifestation.leftSeats == 0 || user.role != "USER" || manifestation.status != "ACTIVE" || date <= today)
		reserve.style.display = "none"

	// Commenting
	if (user == null || user.role != "USER") {
		document.getElementById('comment_form').style.display = "none"
	} else {
		$.ajax({
			type: 'GET',
			contentType: 'application/json',
			url: 'rest/comments/isCommentingAllowed/' + manifestation.id,
			success: function(result) {
				if (manifestation.status != "ACTIVE" || date >= today || !result || user == null) {
					document.getElementById('comment_form').style.display = "none"
				}
			},
			error: function() {
				document.getElementById('comment_form').style.display = "none"
			}
		});
	}

	// Can user see left comments
	if (manifestation.status != "ACTIVE" || date >= today) {
		document.getElementById('comment_section').style.display = "none"
	}

	tickets.max = manifestation.leftSeats

	var lat = parseFloat(manifestation.location.latitude) * 1
	var lon = parseFloat(manifestation.location.longitude) * 1

	if (new Date(manifestation.date) <= new Date() && manifestation.status === "ACTIVE") {
		$.ajax({
			type: 'GET',
			contentType: 'application/json',
			url: 'rest/manifestations/rating/' + manifestation.id,
			success: function(rating) {
				if (rating != -1) {

					let basicInfo = $(
						'<div class="card horizontal">' +
						'<div class="card-image">' +
						'<img src="data:image/png;base64, ' + manifestation.poster + '" alt="Poster"/>' +
						'</div>' +
						'<div class="card-content">' +
						'<span class="card-title">' + manifestation.name + ' - ' + type + '</span>' +
						'<p> Date: ' + new Date(manifestation.date).toUTCString() + '</p>' +
						'<p>Price: ' + manifestation.regularPrice + '</p>' +
						'<p>Status: ' + status + '</p> ' +
						'<p> Number of seats: ' + manifestation.numSeats + '</p>' +
						'<p> Available tickets: ' + manifestation.leftSeats + ' </p> ' +
						'<p> Address: ' + manifestation.location.address + ' (' + lon + ',' + lat + ')</p>' +
						'<p style="padding-bottom:20px;">Rating:' + rating + ' </p>' +
						'</div>' +
						'</div>');

					regular = manifestation.regularPrice
					document.getElementById('total').value = regular
					$('#manifestaion').append(basicInfo);
				}

			}
		});
	} else {
		let basicInfo = $(
			'<div class="card horizontal">' +
			'<div class="card-image">' +
			'<img src="data:image/png;base64, ' + manifestation.poster + '" alt="Poster"/>' +
			'</div>' +
			'<div class="card-content">' +
			'<span class="card-title">' + manifestation.name + ' - ' + type + '</span>' +
			'<p> Date: ' + new Date(manifestation.date).toUTCString() + '</p>' +
			'<p>Price: ' + manifestation.regularPrice + '</p>' +
			'<p>Status: ' + status + '</p> ' +
			'<p> Number of seats: ' + manifestation.numSeats + '</p>' +
			'<p> Available tickets: ' + manifestation.leftSeats + ' </p> ' +
			'<p style="padding-bottom:20px;"> Address: ' + manifestation.location.address + ' (' + lon + ',' + lat + ')</p>' +
			'</div>' +
			'</div>');

		regular = manifestation.regularPrice
		document.getElementById('total').value = regular
		$('#manifestaion').append(basicInfo);
	}

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


	$.ajax({
		type: 'GET',
		contentType: 'application/json',
		url: 'rest/comments/list/' + manifestation.id,
		success: function(result) {
			for (let c of result)
				showComment(c, user)

		}
	});

}


$(document).ready(function() {
	const queryString = window.location.search;
	const urlParams = new URLSearchParams(queryString);
	const id = urlParams.get('manifestation')



	$.ajax({
		type: 'GET',
		url: "rest/tickets/discount",
		contentType: 'application/json',
		success: function(result) {
			console.log(result);
			discount = result

		}
	});


	$.ajax({
		type: 'GET',
		contentType: 'application/json',
		url: 'rest/manifestations/' + id,
		success: function(manifestaton) {
			$.ajax({
				type: 'GET',
				contentType: 'application/json',
				url: 'rest/registration/registeredUser',
				success: function(user) {
					if (user != null) {
						if (user.role == "USER") {
							document.getElementById("li_manifestations").innerHTML = ''
							document.getElementById("li_users").innerHTML = ''
						}
						document.getElementById("li_registration").innerHTML = ''
						document.getElementById("li_login").innerHTML = ''
						showManifestation(manifestaton, user)
					}
					else {
						document.getElementById("li_users").innerHTML = ''
						document.getElementById("li_my_profile").innerHTML = ''
						document.getElementById("li_tickets").innerHTML = ''
						document.getElementById("li_manifestations").innerHTML = ''
						document.getElementById("li_logout").innerHTML = ''
						showManifestation(manifestaton, null)
					}
				},
				error: function() {
					document.getElementById("li_users").innerHTML = ''
					document.getElementById("li_my_profile").innerHTML = ''
					document.getElementById("li_tickets").innerHTML = ''
					document.getElementById("li_manifestations").innerHTML = ''
					document.getElementById("li_logout").innerHTML = ''
					showManifestation(manifestaton, null)
				},
			});
		}
	});
});