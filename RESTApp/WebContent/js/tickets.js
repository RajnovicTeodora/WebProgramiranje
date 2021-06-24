function cancelTicket(ticket) {
	$.get({
		url: 'rest/tickets/cancel/' + ticket.id,
		success: function() {
			document.getElementById(ticket.id).outerHTML = ''
			M.toast({ html: 'Successfully canceled ticket', classes: 'rounded', panning: 'center' });
		},
		error: function() {
			M.toast({ html: 'Unable to cancel ticket', classes: 'rounded', panning: 'center' });
		}
	});


}


function addTicketTr(ticket, role) {

	var cancel = document.getElementById('cancel_th');

	let tr = $('<tr id="' + ticket.id + '"></tr>');
	let tdManifestation = $('<td>' + ticket.manifestation + '</td>');
	let tdDate = $('<td>' + new Date(ticket.date).toString() + '</td>');
	let tdPrice = $('<td>' + ticket.price + '</td>');
	let tdStatus = $('<td>' + ticket.status + '</td>');
	let tdType = $('<td>' + ticket.type + '</td>');

	tr.append(tdManifestation).append(tdDate).append(tdPrice).append(tdStatus).append(tdType);
	if (role === "USER") {
		var date = new Date(ticket.date);
		var today = new Date();
		today.setDate(today.getDate() + 7);
		if (date > today) {
			
			var btn = document.createElement('input');
			btn.type = "button";
			btn.className = "btn";
			btn.value = "Cancel";
			btn.onclick = (function(ticket) { return function() { cancelTicket(ticket); } })(ticket);
			let tdBtn = $('<td></td>');
			tdBtn.append(btn);
			tr.append(tdBtn);
		}
	} else {
		cancel.style.display = "none";
	}

	$('#tickets tbody').append(tr);
}


$(document).ready(function() {
	$.get({
		url: 'rest/registration/registeredUser',
		success: function(registeredUser) {
			user = registeredUser;
		}
	});

	$.get({
		url: 'rest/tickets/list',
		success: function(tickets) {
			$.get({
				url: 'rest/registration/registeredUser',
				success: function(registeredUser) {
					var user = registeredUser;
					for (let ticket of tickets) {
						addTicketTr(ticket, user.role);
					}
				},
				ererror: function() {

					for (let ticket of tickets) {
						addTicketTr(ticket, " ");
					}
				}
			});
		}
	});

});