function addTicketTr(ticket) {
	let tr = $('<tr></tr>');
	let tdManifestation = $('<td>' + ticket.manifestation.name + '</td>');
	let tdDate = $('<td>' + ticket.date + '</td>');
	let tdTime = $('<td>' + ticket.time + '</td>');
	let tdPrice = $('<td>' + ticket.price + '</td>');
	let tdStatus = $('<td>' + ticket.status + '</td>');
	let tdType = $('<td>' + ticket.type + '</td>');

	tr.append(tdManifestation).append(tdDate).append(tdTime).append(tdPrice).append(tdStatus).append(tdType);
	$('#tickets tbody').append(tr);
}

function showInfo(user) {
	if (!user) return;

	document.getElementById('username').value = user.username;
	document.getElementById('password').value = user.password;
	document.getElementById('name').value = user.firstName;
	document.getElementById('surname').value = user.lastName;
	document.getElementById('birthday').value = user.birthday;
	document.getElementById('gender').value = user.gender.charAt(0).toUpperCase() + user.gender.slice(1).toLowerCase();
	
	if (user.role == "VENDOR") {

	}
	if (user.role == "USER") {
		document.getElementById('points_field').hidden = false;
		document.getElementById('points').value = user.points;

		for (let ticket of user.tickets) {
			addTicketTr(ticket);
		}

	}
}

$(document).ready(function () {
	$.get({
		url: 'rest/registration/registeredUser',
		success: function (user) {
			showInfo(user);
		}
	});

});