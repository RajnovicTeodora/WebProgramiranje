/**
 * 
 */function addManifestationCard(manifestation) {
	
	let card = $('<div class="col s4">' +
		 		 '<div class="card">' +
				 '<div class="card-image">' +
				 '<img src="Resources/posters/ticket.jpg"">' +
			     '<a class="btn-floating halfway-fab waves-effect waves-light teal darken-2 ligten" href="http://localhost:8080/RESTApp/manifestation.html?manifestation=' + manifestation.id + '"><i class="material-icons">info</i></a>' +
				 '</div>' +
				 '<div class="card-content">' + 
				 '<span class="card-title">' + manifestation.name + ' - ' + manifestation.type  + '</span>' +
				 '<p> Date: ' + new Date(manifestation.date).toUTCString() + '</p><p>Price: ' +
				 manifestation.regularPrice + '</p><p>Status: ' + manifestation.status + '</p> ' +
				 '</div></div></div></div>');

	$('#manifestaions').append(card);
}


$(document).ready(function () {
	$.get({
		url: 'rest/manifestations/list',
		success: function (manifestatons) {
			console.log(manifestatons)
			for (let manifestation of manifestatons) {
				addManifestationCard(manifestation);
			}
		}
	});

});