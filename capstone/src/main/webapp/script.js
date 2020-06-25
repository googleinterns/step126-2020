async function associationUpdateDisplay() {
  const response = await fetch('/associations');
  const associations = await response.json();
  
  const positive = document.getElementById('pos-associations');
  positive.innerHTML = '';
  associations.positive.forEach(elem => addListElement(positive, elem));

  const negative = document.getElementById('neg-associations');
  negative.innerHTML = '';
  associations.negative.forEach(elem => addListElement(negative, elem));
}

function addListElement(list, contents) {
  const elem = document.createElement('li');
  elem.textContent = contents;
  list.appendChild(elem);
}

window.addEventListener('load', associationUpdateDisplay)
