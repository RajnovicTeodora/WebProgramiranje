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

	document.getElementById('username').innerHTML = user.username;
	document.getElementById('password').innerHTML = user.password;
	document.getElementById('name').innerHTML = user.firstName;
	document.getElementById('surname').innerHTML = user.lastName;
	document.getElementById('birthday').innerHTML = user.birthday.year + '-' + user.birthday.monthValue + '-' + user.birthday.dayOfMonth;
	document.getElementById('gender').innerHTML = user.gender.charAt(0).toUpperCase() + user.gender.slice(1).toLowerCase();
	
	if (user.role == "VENDOR") {

	}
	if (user.role == "USER") {
		let points = $("<div class=\"col s3\"><p>Gathered points</p></div>" +
			"<div class=\"col s3\"><p>" + user.points + "</p></div>");
		$('#points').append(points);

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