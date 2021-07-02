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

	$('#vendor_manifestations tbody').append(tr);
}

function addManifestationTrAdmin(manifestation) {

	var date = new Date(manifestation.date)
	var today = new Date()

	$.ajax({
		type: 'GET',
		url: "rest/manifestations/isDeleteAllowed/" + manifestation.id,
		contentType: 'application/json',
		success: function(response) {
			let tr = $('<tr id="' + manifestation.id + '"></tr>');
			let tdName = $('<td>' + manifestation.name + '</td>');
			let tdType = $('<td>' + manifestation.type + '</td>');
			let tdDate = $('<td>' + new Date(manifestation.date).toUTCString() + '</td>');
			let tdPrice = $('<td>' + manifestation.regularPrice + '</td>');
			let tdSeats = $('<td>' + manifestation.numSeats + '</td>');
			let tdAddress = $('<td>' + manifestation.location.address + '</td>');
			let tdApprove = $('<td></td>');
			let tdDelete;

			if (response) {


				tdDelete = $('<td><button onclick="deleteManifestation(' + manifestation.id + ')" class="secondary-content btn-floating btn-small waves-effect waves-light"><i class="material-icons">delete</i></button></td>');

			} else {

				tdDelete = $('<td></td>');

			}

			// Date has not passed and manifestation is not active
			if (date > today && manifestation.status != "ACTIVE") {
				tdApprove = $('<td><button onclick="approve(' + manifestation.id + ')" class="secondary-content btn-floating btn-small waves-effect waves-light"><i class="material-icons">check_circle</i></button></td>');
			}

			tr.append(tdName).append(tdType).append(tdDate).append(tdPrice).append(tdSeats).append(tdAddress).append(tdApprove).append(tdDelete);

			$('#admin_manifestations tbody').append(tr);
		}
	});
}


function deleteManifestation(id) {
	$.ajax({
		type: 'GET',
		url: "rest/manifestations/delete/" + id,
		contentType: 'application/json',
		success: function(manifestation) {
			document.getElementById(manifestation.id).innerHTML = ''
			M.toast({ html: 'Successfully deleted manifestation', classes: 'rounded', panning: 'center' });
		}, error: function() {
			M.toast({ html: 'Unable to delete manifestation', classes: 'rounded', panning: 'center' });
		}
	});
}

function approve(id) {
	$.ajax({
		type: 'GET',
		contentType: 'application/json',
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
		$.ajax({
			type: 'GET',
			contentType: 'application/json',
			url: 'rest/vendor/manifestations',
			success: function(manifestations) {

				document.getElementById("admin_manifestations").style.display = "none"
				for (let manifestation of manifestations) {
					addManifestationTrVendor(manifestation);
				}
			}
		});
	} else if (user.role == "ADMINISTRATOR") {
		$.ajax({
			type: 'GET',
			contentType: 'application/json',
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
	$.ajax({
		type: 'GET',
		contentType: 'application/json',
		url: 'rest/registration/registeredUser',
		success: function(user) {
			if(user != null)
				showInfo(user);

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
	let priceFrom = $('input[name="priceFrom"]').val();
	let priceTo = $('input[name="priceTo"]').val();

	console.log(dateFrom);

	if (dateFrom !== "" && dateTo !== "") {
		if (dateFrom > dateTo) {
			M.toast({ html: 'Date from must be before date to!' })
			return;
		}
	}

	if (priceFrom !== "" && priceTo !== "") {
		if (priceFrom > priceTo) {
			M.toast({ html: 'Price from must be leser than price to.', classes: 'rounded', panning: 'center' });
			return;
		}
	}

	if (manifestationName === "") manifestationName = "null";
	if (location === "") location = "null";
	if (dateFrom === "") dateFrom = "null";
	if (dateTo === "") dateTo = "null";
	if (priceFrom === "") priceFrom = "null";
	if (priceTo === "") priceTo = "null";


	console.log("Sending request...");
	$.ajax({
		type: 'GET',
		url: "rest/manifestations/searchManifestations/" + manifestationName + "/" + location + "/" + dateFrom + "/" + dateTo + "/" + priceFrom + "/" + priceTo,
		contentType: 'application/json',
		success: function(response) {
			$('vendor_manifestations').empty();
			console.log(response);
			for (let manifestation of response) {
				addManifestationTrVendor(manifestation);
			}
			M.toast({ html: 'Successfully sent data.', classes: 'rounded', panning: 'center' });

		},
		error: function() {
			M.toast({ html: 'Failed to send data', classes: 'rounded', panning: 'center' });
		}
	});

});