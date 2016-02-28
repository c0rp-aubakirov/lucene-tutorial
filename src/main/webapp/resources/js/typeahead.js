$(document).ready(function () {
    var autocomplete = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '../api/autocomplete/%QUERY',
            wildcard: '%QUERY'
        }
    });

    autocomplete.initialize();

    $('#remote .typeahead').typeahead({
        hint: true,
        highlight: true,
        minLength: 1
    }, {
        name: 'value',
        display: 'value',
        source: autocomplete
    });
});
