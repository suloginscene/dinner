const ajaxTopicArticles = function () {

    const topicId = $('#topic-id').text();
    const articles = $('#articles');

    const articleTemplate = document.querySelector("#article-template").innerHTML;
    const tagTemplate = document.querySelector("#tag-template").innerHTML;


    const appendArticles = function (articleArr) {
        articleArr.forEach(function (a) {
            const date = a.createdAt.substring(0, "yyyy-MM-dd".length);
            const article = articleTemplate.replace("{id}", a.id).replace("{title}", a.title)
                .replace("{writer}", a.owner).replace("{writer}", a.owner).replace("{createdAt}", date)
                .replace("{tags}", function () {
                    const tags = a.tags;
                    let tagListString = "";
                    tags.forEach(function (t) {
                        const renderedTag = tagTemplate.replace("{tag}", t).replace("{tag}", t);
                        tagListString += renderedTag;
                    });
                    return tagListString;
                });
            articles.append(article);
        });
    };


    $.ajax({
        type: 'get',
        url: '/api/articles/of/' + topicId,
        success: appendArticles
    });

};


window.addEventListener('load', ajaxTopicArticles);
