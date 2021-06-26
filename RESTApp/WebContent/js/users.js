function addUserTr(user) {

	let tr = $('<tr id="' + user.username + '"></tr>');
	let tdUsername = $('<td>' + user.username + '</td>');
	let tdName = $('<td>' + user.firstName + '</td>');
	let tdSurname = $('<td>' + user.lastName + '</td>');
	let tdGender = $('<td>' + user.gender + '</td>');
	let tdBirthday = $('<td>' + user.birthday + '</td>');
	let tdRole = $('<td>' + user.role + '</td>');
	let tdType = $('<td>' + user.customerType + '</td>');
	let tdPoints = $('<td>' + user.points + '</td>');

	tr.append(tdUsername).append(tdName).append(tdSurname).append(tdGender).append(tdBirthday).append(tdRole).append(tdType).append(tdPoints);

	// TODO ADD DELETE BUTTON ?

	$('#users tbody').append(tr);
}

function showInfo(user) {
	if (!user) return;

	$.get({
		url: 'rest/registration/users',
		success: function(users) {
			if (user.role == "VENDOR")
				document.getElementById("new_vendor").innerHTML = ''
			for (let u of users) {
				addUserTr(u);
			}
		}
	});

}

$(document).ready(function() {
	$.get({
		url: 'rest/registration/registeredUser',
		success: function(user) {
			if (user.role == "ADMINISTRATOR" || user.role == "VENDOR")
				showInfo(user);
		}
	});

});