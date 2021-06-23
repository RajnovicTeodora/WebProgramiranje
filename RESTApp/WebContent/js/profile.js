function cancelTicket(ticket){
	$.get({
		url: 'rest/tickets/cancel/' + ticket.id,
		success: function () {
			document.getElementById(ticket.id).outerHTML = ''
			M.toast({ html: 'Successfully canceled ticket', classes: 'rounded', panning: 'center' });
		},
		error: function () {
			M.toast({ html: 'Unable to cancel ticket', classes: 'rounded', panning: 'center' });
		}
	});


}

function addTicketTr(ticket, role) {
	
	var cancel = document.getElementById('cancel_th');
	
	let tr = $('<tr id="' + ticket.id + '"></tr>');
	let tdManifestation = $('<td>' + ticket.manifestation + '</td>');
	let tdDate = $('<td>' + ticket.date + '</td>');
	let tdPrice = $('<td>' + ticket.price + '</td>');
	let tdStatus = $('<td>' + ticket.status + '</td>');
	let tdType = $('<td>' + ticket.type + '</td>');
	
	tr.append(tdManifestation).append(tdDate).append(tdPrice).append(tdStatus).append(tdType);
	if(role === "USER"){
		var date = new Date(ticket.date);
		var today = new Date();
		today.setDate(today.getDate()+7);
		console.log(today);
		console.log(date);
		if(date > today ){
			var btn = document.createElement('input');
			btn.type = "button";
			btn.className = "btn";
			btn.value = "Cancel";
			btn.onclick = (function(ticket) {return function() {cancelTicket(ticket);}})(ticket);
			let tdBtn = $('<td></td>');
			tdBtn.append(btn);
			tr.append(tdBtn);
		}
	}else{
		cancel.style.display = "none";
	}

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

	}
	for (let ticket of user.tickets) {
		addTicketTr(ticket, user.role);
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