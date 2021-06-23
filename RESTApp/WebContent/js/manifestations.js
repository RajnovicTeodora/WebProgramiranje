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
	console.log("Filtering manifestations...");

	let manifestationName = $('input[name="manifestationName"]').val();
	let location = $('input[name="location"]').val();
	let dateFrom = $('input[name="dateFrom"]').val();
	let dateTo = $('input[name="dateTo"]').val();
	let priceFrom = $('input[name="priceFrom"]').val();
	let priceTo = $('input[name="priceTo"]').val();

	if (dateFrom > dateTo) {
		M.toast({ html: 'Date from must be greater than date to!' })
		$('#error').text('Date from must be greater than date to!');
		$("#error").show().delay(3000).fadeOut();
		return;
	}

	if (priceFrom > priceTo) {
		M.toast({ html: 'Successfully registered', classes: 'rounded', panning: 'center' });
		$('#error').text('Price from must be greater than price to!');
		$("#error").show().delay(3000).fadeOut();
		return;
	}

	//TODO send get request

});