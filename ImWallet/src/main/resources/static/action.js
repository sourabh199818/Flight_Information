document.addEventListener("DOMContentLoaded", function() {
  // Define the number of items per page
  var itemsPerPage = 5;

  // Get the table body and rows
  var tableBody = document.querySelector("tbody");
  var rows = Array.from(tableBody.getElementsByTagName("tr"));

  // Calculate the total number of pages
  var totalPages = Math.ceil(rows.length / itemsPerPage);

  // Function to display the selected page
  function showPage(page) {
    // Calculate the starting and ending indices of the items to be displayed
    var startIndex = (page - 1) * itemsPerPage;
    var endIndex = page * itemsPerPage;

    // Hide all rows
    rows.forEach(function(row) {
      row.style.display = "none";
    });

    // Display the rows for the selected page
    var pageRows = rows.slice(startIndex, endIndex);
    pageRows.forEach(function(row) {
      row.style.display = "table-row";
    });
  }

  // Function to create the pagination links
  // Function to create the pagination links
  function createPaginationLinks() {
    // Get the pagination div
    var paginationDiv = document.getElementById("pagination");

    // Clear any existing content in the pagination div
    paginationDiv.innerHTML = "";

    // Create the previous button
    var prevButton = document.createElement("button");
    prevButton.textContent = "<";
    prevButton.addEventListener("click", function() {
      var currentPage = getCurrentPage();
      if (currentPage > 1) {
        showPage(currentPage - 1);
      }
    });
    paginationDiv.appendChild(prevButton);

    // Calculate the range of visible page numbers
    var currentPage = getCurrentPage();
    var startPage = Math.max(currentPage - 2, 1);
    var endPage = Math.min(currentPage + 2, totalPages);

    // Create the page buttons
    for (var i = startPage; i <= endPage; i++) {
      var button = document.createElement("button");
      button.textContent = i;
      button.addEventListener("click", function() {
        var selectedPage = parseInt(this.textContent);
        showPage(selectedPage);
      });
      paginationDiv.appendChild(button);
    }

    // Create the next button
    var nextButton = document.createElement("button");
    nextButton.textContent = ">";
    nextButton.addEventListener("click", function() {
      var currentPage = getCurrentPage();
      if (currentPage < totalPages) {
        showPage(currentPage + 1);
      }
    });
    paginationDiv.appendChild(nextButton);
  }


  // Function to get the current page number
  function getCurrentPage() {
    var activeButton = document.querySelector("#pagination button.active");
    if (activeButton) {
      return parseInt(activeButton.textContent);
    }
    return 1;
  }

  // Display the first page by default
  showPage(1);

  // Create the pagination links
  createPaginationLinks();
});
