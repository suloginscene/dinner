window.addEventListener('load', function () {

    const magazineId = $('#magazine-id').text();
    const topics = $('#topics');

    const topicTemplate = document.querySelector("#topic-template").innerHTML;

    const appendTopics = function (topicArr) {
        topicArr.forEach(function (t) {
            const el = topicTemplate.replace("{id}", t.id).replace("{title}", t.title);
            topics.append(el);
        });
    };


    $.ajax({
        type: 'get',
        url: '/api/topics/of/' + magazineId,
        success: appendTopics
    });

});
