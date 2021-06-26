function addManifestationCard(manifestation) {
	let card = $('<div class="col s4">' +
		'<div class="card">' +
		'<div class="card-image">' +
		'<img src="Resources/posters/ticket.jpg">' +
		'<a class="btn-floating halfway-fab waves-effect waves-light teal darken-2 ligten" href="http://localhost:8080/RESTApp/manifestation.html?manifestation=' + manifestation.id + '"><i class="material-icons">info</i></a>' +
		'</div>' +
		'<div class="card-content">' +
		'<span class="card-title">' + manifestation.name + ' - ' + manifestation.type + '</span>' +
		'<p> Date: ' + new Date(manifestation.date).toUTCString() + '</p><p>Price: ' +
		manifestation.regularPrice + '</p><p>Status: ' + manifestation.status + '</p></p>Location: ' + manifestation.location.address + '</p>' +
		'</div></div></div></div>');

	$('#manifestaions').append(card);
}


$(document).ready(function() {

	$.get({
		url: 'rest/registration/registeredUser',
		success: function(user) {
			if (user.role == "USER") {
				document.getElementById("li_manifestations").innerHTML = ''
				document.getElementById("li_users").innerHTML = ''
			}
			document.getElementById("li_registration").innerHTML = ''
			document.getElementById("li_login").innerHTML = ''
		},
		error: function() {
			document.getElementById("li_users").innerHTML = ''
			document.getElementById("li_my_profile").innerHTML = ''
			document.getElementById("li_tickets").innerHTML = ''
			document.getElementById("li_manifestations").innerHTML = ''
			document.getElementById("li_logout").innerHTML = ''
		}
	});


	$.get({
		url: 'rest/manifestations/list',
		success: function(manifestatons) {
			console.log(manifestatons)
			for (let manifestation of manifestatons) {
				addManifestationCard(manifestation);
			}
		}
	});

});



// TODO 
$("#filter_manifestations_form").submit(function(event) {

	// Stop form from submitting normally
	event.preventDefault();
	console.log("Filtering manifestations...");

	let manifestationName = $('input[name="manifestationName"]').val();
	let location = $('input[name="location"]').val();
	let dateFrom = $('input[name="dateFrom"]').val();
	let dateTo = $('input[name="dateTo"]').val();
	let priceFrom = $('input[name="priceFrom"]').val();
	let priceTo = $('input[name="priceTo"]').val();

	if (dateFrom > dateTo) {
		M.toast({ html: 'Date from must be greater than date to!' })
		return;
	}

	if (priceFrom > priceTo) {
		M.toast({ html: 'Price from must be greater than price to.', classes: 'rounded', panning: 'center' });
		return;
	}

	//TODO send get request

});