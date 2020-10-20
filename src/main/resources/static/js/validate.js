function validate() {

    const form = document.getElementById('signup-form');

    const checkStrings = function (e) {
        if (form.checkValidity() === false) {
            e.preventDefault();
            e.stopPropagation();
        }
        form.classList.add('was-validated');
    };


    const term = document.getElementById('term');

    const checkTerm = function (e) {
        const termHint = document.getElementById('term-hint');
        if (!term.checked) {
            e.preventDefault();
            termHint.style.display = 'block';
        } else {
            termHint.style.display = 'none';
        }
    }


    form.addEventListener('input', checkStrings, false);
    term.addEventListener('input', checkTerm, false);

    form.addEventListener('submit', checkStrings, false);
    form.addEventListener('submit', checkTerm, false);

}

window.addEventListener('load', validate);
