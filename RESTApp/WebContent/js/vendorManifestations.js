/**
 * '<a class="btn-floating right waves-effect waves-light teal darken-2" href="http://localhost:8080/RESTApp/editManifestation.html?manifestation=' + manifestation.id + '"><i class="material-icons">edit</i></a>' +
 */

function addManifestationTr(manifestation) {

	let tr = $('<tr id="' + manifestation.id + '"></tr>');
	let tdName = $('<td>' + manifestation.name + '</td>');
	let tdType = $('<td>' + manifestation.type + '</td>');
	let tdDate = $('<td>' + new Date(manifestation.date).toUTCString() + '</td>');
	let tdPrice = $('<td>' + manifestation.regularPrice + '</td>');
	let tdStatus = $('<td>' + manifestation.status + '</td>');
	let tdSeats = $('<td>' + manifestation.numSeats + '</td>');
	let tdLeft = $('<td>' + manifestation.leftSeats + '</td>');
	let tdAddress = $('<td>' + manifestation.location.address + '</td>');

	tr.append(tdName).append(tdType).append(tdDate).append(tdPrice).append(tdStatus).append(tdSeats).append(tdLeft).append(tdAddress);
	
	var date = new Date(manifestation.date)
	var today = new Date()

	if (date > today) {
		let tdEdit = $('<td><a class="btn-floating right waves-effect waves-light teal darken-2" href="http://localhost:8080/RESTApp/editManifestation.html?manifestation=' + manifestation.id + '"><i class="material-icons">edit</i></a></td>');
		tr.append(tdEdit)
	}



	// TODO ADD DELETE BUTTON ?

	$('#manifestations tbody').append(tr);
}

function showInfo(user) {
	if (!user) return;

	$.get({
		url: 'rest/vendor/manifestations',
		success: function(manifestations) {
			for (let manifestation of manifestations) {
				addManifestationTr(manifestation);
			}
		}
	});

}

$(document).ready(function() {
	$.get({
		url: 'rest/registration/registeredUser',
		success: function(user) {
			if (user.role == "VENDOR")
				showInfo(user);
		}
	});

});