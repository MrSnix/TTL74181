// We specify the PDF worker thread
pdfjsLib.GlobalWorkerOptions.workerSrc = '../js/pdf.worker.js';
// The main PDFLibrary implementation
const PDFLibrary = {
    // Attributes
    data64: data,
    docs: null,
    total_pages: 0,
    current_page: 0,
    render_flag: false,
    scale: 1,
    canvas: document.getElementById('viewer-pdf'),
    // Methods
    init: function () {
        // Loading the document raw data
        const task = pdfjsLib.getDocument({data: PDFLibrary.data64});
        // Executing task in order to retrieve the pdf object
        task.promise.then(document => {
            // Saving object
            PDFLibrary.docs = document;
            // Acquiring total pages
            PDFLibrary.total_pages = document.numPages;
            // Setting current page as first
            PDFLibrary.current_page = 1;
            // Drawing to refresh controls properties
            PDFLibrary.draw();
            // Asking to render the current page
            PDFLibrary.render()
        });

        // Adding event listeners
        document.getElementById('next').addEventListener('click', PDFLibrary.next);
        document.getElementById('prev').addEventListener('click', PDFLibrary.prev);
        // Adding event listeners
        document.getElementById('zoom-in').addEventListener('click', PDFLibrary.zoom_in);
        document.getElementById('zoom-out').addEventListener('click', PDFLibrary.zoom_out);
    },
    render: function () {

        // Enabling rendering mode
        PDFLibrary.render_flag = true;
        // Fetching the page
        PDFLibrary.docs.getPage(PDFLibrary.current_page).then(function (page) {

            const processing_page = PDFLibrary.current_page;

            // Acquiring viewport
            const viewport = page.getViewport({scale: PDFLibrary.scale});

            // Resizing canvas according to the viewport
            PDFLibrary.canvas.height = viewport.height;
            PDFLibrary.canvas.width = viewport.width;

            // Acquiring the render context
            const renderer = {
                canvasContext: PDFLibrary.canvas.getContext('2d'),
                viewport: viewport
            };

            // Acquiring the render task
            const task = page.render(renderer);

            // Wait for rendering to finish
            task.promise.then(function () {
                // Updating flag
                PDFLibrary.render_flag = false;
                // If the current page is not null and the last page rendered is different from the current page
                if (PDFLibrary.current_page !== null && PDFLibrary.current_page !== processing_page) {
                    // The current page status has been updated, rendering
                    PDFLibrary.render();
                }
            });

            // Updating controls
            PDFLibrary.draw();
        })
    },
    draw: function () {
        // Drawing total pages number
        document.getElementById('page_count').textContent = PDFLibrary.total_pages;
        // Update page counter
        document.getElementById('page_num').textContent = PDFLibrary.current_page;
    },
    next: function () {
        if (PDFLibrary.current_page + 1 <= PDFLibrary.total_pages) {
            // Updating index
            ++PDFLibrary.current_page;
            // If not rendering, call now the render() function
            if (!PDFLibrary.render_flag)
                PDFLibrary.render();
        }
    },
    prev: function () {
        if (PDFLibrary.current_page - 1 >= 1) {
            // Updating index
            --PDFLibrary.current_page;
            // If not rendering, call now the render() function
            if (!PDFLibrary.render_flag)
                PDFLibrary.render();
        }
    },
    zoom_in: function () {
        if(PDFLibrary.scale + 0.5 <= 3) {
            // Changing scale value
            PDFLibrary.scale += 0.5;
            // If not rendering, call now the render() function
            if (!PDFLibrary.render_flag)
                PDFLibrary.render();
        }
    },
    zoom_out: function () {
        if(PDFLibrary.scale - 0.5 >= 1) {
            // Changing scale value
            PDFLibrary.scale -= 0.5;
            // If not rendering, call now the render() function
            if (!PDFLibrary.render_flag)
                PDFLibrary.render();
        }
    }
};

// Starting
PDFLibrary.init();