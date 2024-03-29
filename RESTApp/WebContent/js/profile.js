
$("#profile_form").submit(function(event) {

	// Stop form from submitting normally
	event.preventDefault();
	console.log("Editing profile...");

	let password = $('input[name="password"]').val();
	let name = $('input[name="name"]').val();
	let surname = $('input[name="surname"]').val();

	if (!password || !name || !surname) {
		M.toast({ html: 'All fields must be filled!', classes: 'rounded', panning: 'center' });
		return;
	}

	$.ajax({
		type: 'POST',
		url: "rest/registration/edit",
		data: JSON.stringify({
			password: password,
			name: name,
			surname: surname

		}),
		contentType: 'application/json',
		success: function(result) {
			console.log(result);
			M.toast({ html: 'Successfully edited profile information', classes: 'rounded', panning: 'center' });

		},
		error: function() {
			M.toast({ html: 'Invalid input', classes: 'rounded', panning: 'center' });
		}
	});
});


function showInfo(user) {
	if (!user) return;

	document.getElementById('username').value = user.username;
	document.getElementById('password').value = user.password;
	document.getElementById('name').value = user.firstName;
	document.getElementById('surname').value = user.lastName;
	document.getElementById('birthday').value = user.birthday;
	document.getElementById('gender').value = user.gender.charAt(0).toUpperCase() + user.gender.slice(1).toLowerCase();

	if (user.role == "USER") {
		document.getElementById('points_field').hidden = false;
		document.getElementById('kind_field').hidden = false;
		document.getElementById('points').value = user.points;
		document.getElementById('kind').value = user.kind;
	}

}

$(document).ready(function() {
	$.ajax({
		type: 'GET',
		contentType: 'application/json',
		url: 'rest/registration/registeredUser',
		success: function(user) {
			if (user != null) {
				if (user.role == "USER") {
					document.getElementById("li_manifestations").innerHTML = ''
					document.getElementById("li_users").innerHTML = ''
				}
				showInfo(user);
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

});