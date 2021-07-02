function cancelTicket(ticket) {
	$.ajax({
		type: 'GET',
		contentType: 'application/json',
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

	$.ajax({
		type: 'GET',
		contentType: 'application/json',
		url: 'rest/registration/registeredUser',
		success: function(u) {
			if (u != null) {
				if (u.role == "USER") {
					user = u
					document.getElementById("li_manifestations").innerHTML = ''
					document.getElementById("li_users").innerHTML = ''
				}
			} else {
				document.getElementById("li_users").innerHTML = ''
				document.getElementById("li_my_profile").innerHTML = ''
				document.getElementById("li_tickets").innerHTML = ''
				document.getElementById("li_manifestations").innerHTML = ''
				document.getElementById("li_logout").innerHTML = ''
			}
		},
		error: function() {
			document.getElementById("li_users").innerHTML = ''
			document.getElementById("li_my_profile").innerHTML = ''
			document.getElementById("li_tickets").innerHTML = ''
			document.getElementById("li_manifestations").innerHTML = ''
			document.getElementById("li_logout").innerHTML = ''
		}
	});

	$.ajax({
		type: 'GET',
		contentType: 'application/json',
		url: 'rest/tickets/list',
		success: function(tickets) {
			$.ajax({
				type: 'GET',
				contentType: 'application/json',
				url: 'rest/registration/registeredUser',
				success: function(registeredUser) {
					if (user != null) {
						var user = registeredUser;
						for (let ticket of tickets) {
							addTicketTr(ticket, user.role);
						}
					} else {
						for (let ticket of tickets) {
							addTicketTr(ticket, " ");
						}
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


$("#filter_tickets_form").submit(function(event) {

	// Stop form from submitting normally
	event.preventDefault();
	console.log("Filtering tickets...");

	let manifestationName = $('input[name="manifestationName"]').val();
	let dateFrom = $('input[name="dateFrom"]').val();
	let dateTo = $('input[name="dateTo"]').val();
	let priceFrom = $('input[name="priceFrom"]').val();
	let priceTo = $('input[name="priceTo"]').val();

	let type = document.getElementById('typeSelect').value;
	let status = document.getElementById('statusSelect').value;

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
	if (dateFrom === "") dateFrom = "null";
	if (dateTo === "") dateTo = "null";
	if (priceFrom === "") priceFrom = "null";
	if (priceTo === "") priceTo = "null";


	console.log("Sending request...");
	$.ajax({
		type: 'GET',
		url: "rest/tickets/searchTickets/" + manifestationName + "/" + dateFrom + "/" + dateTo + "/" + priceFrom + "/" + priceTo + "/" + type + "/" + status,
		contentType: 'application/json',
		success: function(response) {
			$("#tickets tbody").empty()
			for (let ticket of response) {
				console.log(ticket);
				addTicketTr(ticket, "USER");
			}
			//M.toast({ html: 'Successfully sent data.', classes: 'rounded', panning: 'center' });

		},
		error: function() {
			M.toast({ html: 'Failed to send data', classes: 'rounded', panning: 'center' });
		}
	});
});