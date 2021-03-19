$(document).ready(function() {
	$('#forma').submit(function(event) {
		event.preventDefault();
		let name = $('input[name="tekst"]').val();
		$.post({
			url: 'rest/pokusaj',
			success: function(product) {
				$('#success').text(name);
				
				$("#success").show();
			}
		});
	});
});
