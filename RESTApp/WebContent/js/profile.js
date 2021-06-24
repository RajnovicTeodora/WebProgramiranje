
$("#profile_form").submit(function(event) {

	// Stop form from submitting normally
	event.preventDefault();
	console.log("Editing profile...");

	let password = $('input[name="password"]').val();
	let name = $('input[name="name"]').val();
	let surname = $('input[name="surname"]').val();

	if (!password || !name || !surname) {
		$('#error').text('All fields must be filled!');
		$("#error").show().delay(3000).fadeOut();
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

	if (user.role == "VENDOR") {

	}
	if (user.role == "USER") {
		document.getElementById('points_field').hidden = false;
		document.getElementById('points').value = user.points;

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