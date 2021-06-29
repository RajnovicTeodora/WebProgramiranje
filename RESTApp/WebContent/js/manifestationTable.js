/**
 * '<a class="btn-floating right waves-effect waves-light teal darken-2" href="http://localhost:8080/RESTApp/editManifestation.html?manifestation=' + manifestation.id + '"><i class="material-icons">edit</i></a>' +
 */

function addManifestationTrVendor(manifestation) {

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

	$('#vendor_manifestations tbody').append(tr);
}

function addManifestationTrAdmin(manifestation) {

	var date = new Date(manifestation.date)
	var today = new Date()

	if (date < today) return

	let tr = $('<tr id="' + manifestation.id + '"></tr>');
	let tdName = $('<td>' + manifestation.name + '</td>');
	let tdType = $('<td>' + manifestation.type + '</td>');
	let tdDate = $('<td>' + new Date(manifestation.date).toUTCString() + '</td>');
	let tdPrice = $('<td>' + manifestation.regularPrice + '</td>');
	let tdSeats = $('<td>' + manifestation.numSeats + '</td>');
	let tdAddress = $('<td>' + manifestation.location.address + '</td>');

	let tdApprove = $('<td><button onclick="approve(' + manifestation.id + ')" class="secondary-content btn-floating btn-small waves-effect waves-light"><i class="material-icons">check_circle</i></button></td>');

	tr.append(tdName).append(tdType).append(tdDate).append(tdPrice).append(tdSeats).append(tdAddress).append(tdApprove);

	$('#admin_manifestations tbody').append(tr);
}

function approve(id) {
	$.get({
		url: 'rest/manifestations/approve/' + id,
		success: function(manifestation) {
			document.getElementById(manifestation.id).innerHTML = ''
			M.toast({ html: 'Successfully approved manifestation', classes: 'rounded', panning: 'center' });
		},
		error: function() {
			M.toast({ html: 'Unable to approve manifestation', classes: 'rounded', panning: 'center' });
		}
	});
}
function showInfo(user) {
	if (!user) return;

	if (user.role == "VENDOR") {
		$.get({
			url: 'rest/vendor/manifestations',
			success: function(manifestations) {

				document.getElementById("admin_manifestations").style.display = "none"
				for (let manifestation of manifestations) {
					addManifestationTrVendor(manifestation);
				}
			}
		});
	} else if (user.role == "ADMINISTRATOR") {
		$.get({
			url: 'rest/admin/manifestations',
			success: function(manifestations) {
				document.getElementById("vendor_manifestations").style.display = "none"
				document.getElementById("new_manifestation_button").style.display = "none"
				for (let manifestation of manifestations) {
					addManifestationTrAdmin(manifestation);
				}
			}
		});
	}

}

$(document).ready(function() {
	$.get({
		url: 'rest/registration/registeredUser',
		success: function(user) {
			showInfo(user);

		}
	});

});