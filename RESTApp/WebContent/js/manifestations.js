
function addManifestationCard(manifestation) {


	if (new Date(manifestation.date) <= new Date() && manifestation.status == "ACTIVE") {
		$.ajax({
			type: 'GET',
			contentType: 'application/json',
			url: 'rest/manifestations/rating/' + manifestation.id,
			success: function(rating) {
				if (rating != -1) {
					let type = manifestation.type.charAt(0) + manifestation.type.toLowerCase().slice(1)
					let status = manifestation.status.charAt(0) + manifestation.status.toLowerCase().slice(1)
					let card = $('<div class="col s4">' +
						'<div class="card">' +
						'<div class="card-image">' +
						'<img src="data:image/png;base64, ' + manifestation.poster + '" alt="Poster">' +
						'<a class="btn-floating halfway-fab waves-effect waves-light teal darken-2 ligten" href="http://localhost:8080/RESTApp/manifestation.html?manifestation=' + manifestation.id + '"><i class="material-icons">info</i></a>' +
						'</div>' +
						'<div class="card-content">' +
						'<span class="card-title">' + manifestation.name + ' - ' + type + '</span>' +
						'<p> Date: ' + new Date(manifestation.date).toUTCString() + '</p><p>Price: ' +
						manifestation.regularPrice + '</p><p>Status: ' + status + '</p></p>Location: ' + manifestation.location.address + ' (' + manifestation.location.longitude + ',' + manifestation.location.latitude + ')</p><p>Rating:' + rating +
						'</p></div></div></div></div>');

					$('#manifestaions').append(card);
				}

			}
		});
	} else {
		let type = manifestation.type.charAt(0) + manifestation.type.toLowerCase().slice(1)
		let status = manifestation.status.charAt(0) + manifestation.status.toLowerCase().slice(1)
		let card = $('<div class="col s4">' +
			'<div class="card">' +
			'<div class="card-image">' +
			'<img src="data:image/png;base64, ' + manifestation.poster + '" alt="Poster">' +
			'<a class="btn-floating halfway-fab waves-effect waves-light teal darken-2 ligten" href="http://localhost:8080/RESTApp/manifestation.html?manifestation=' + manifestation.id + '"><i class="material-icons">info</i></a>' +
			'</div>' +
			'<div class="card-content">' +
			'<span class="card-title">' + manifestation.name + ' - ' + type + '</span>' +
			'<p> Date: ' + new Date(manifestation.date).toUTCString() + '</p><p>Price: ' +
			manifestation.regularPrice + '</p><p>Status: ' + status + '</p></p>Location: ' + manifestation.location.address + ' (' + manifestation.location.longitude + ',' + manifestation.location.latitude + ')</p>' +
			'</div></div></div></div>');

		$('#manifestaions').append(card);
	}
}


$(document).ready(function() {

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
			} else {
				document.getElementById("li_users").innerHTML = ''
				document.getElementById("li_my_profile").innerHTML = ''
				document.getElementById("li_tickets").innerHTML = ''
				document.getElementById("li_manifestations").innerHTML = ''
				document.getElementById("li_logout").innerHTML = ''
			}

		},
		error: function() {
			document.getElementById("li_users").innerHTML = ''
			document.getElementById("li_my_profile").innerHTML = ''
			document.getElementById("li_tickets").innerHTML = ''
			document.getElementById("li_manifestations").innerHTML = ''
			document.getElementById("li_logout").innerHTML = ''
		}
	});


	$.ajax({
		type: 'GET',
		contentType: 'application/json',
		url: 'rest/manifestations/list',
		success: function(manifestatons) {
			console.log(manifestatons)
			for (let manifestation of manifestatons) {
				addManifestationCard(manifestation);
			}
		}
	});

});


$("#filter_manifestations_form").submit(function(event) {

	// Stop form from submitting normally
	event.preventDefault();
	console.log("Filtering manifestationss...");

	let manifestationName = $('input[name="manifestationName"]').val();
	let location = $('input[name="location"]').val();
	let dateFrom = $('input[name="dateFrom"]').val();
	let dateTo = $('input[name="dateTo"]').val();
	let pFrom = document.getElementById('priceFrom');
	let pTo = document.getElementById('priceTo');

	let type = document.getElementById('typeSelect').value;
	let soldOut = document.getElementById('soldOutSelect').value;


	if (dateFrom !== "" && dateTo !== "") {
		if (dateFrom > dateTo) {
			M.toast({ html: 'Date from must be before date to!' })
			return;
		}
	}

	if (pFrom !== "" && pTo !== "") {
		if (pFrom > pTo) {
			M.toast({ html: 'Price from must be leser than price to.', classes: 'rounded', panning: 'center' });
			return;
		}
	}

	let priceFrom = $('input[name="priceFrom"]').val();
	let priceTo = $('input[name="priceTo"]').val();
	
	if (manifestationName === "") manifestationName = "null";
	if (location === "") location = "null";
	if (dateFrom === "") dateFrom = "null";
	if (dateTo === "") dateTo = "null";
	if (priceFrom === "") priceFrom = "null";
	if (priceTo === "") priceTo = "null";


	console.log("Sending request...");
	$.ajax({
		type: 'GET',
		url: "rest/manifestations/searchManifestations/" + manifestationName + "/" + location + "/" + dateFrom + "/" + dateTo + "/" + priceFrom + "/" + priceTo + "/" + type + "/" + soldOut,
		contentType: 'application/json',
		success: function(response) {
			$('#manifestaions').empty();
			console.log(response);
			for (let manifestation of response) {
				console.log(manifestation);
				addManifestationCard(manifestation);
			}
			//M.toast({ html: 'Successfully sent data.', classes: 'rounded', panning: 'center' });

		},
		error: function() {
			M.toast({ html: 'Failed to send data', classes: 'rounded', panning: 'center' });
		}
	});
});