import { Component, ElementRef, Input, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

/**
 * Compatibility component: preserves the original HTML and vanilla-JS behaviour
 * exactly, without Angular rewriting or sanitizing the template's SVG or markup.
 * Every page navigation uses a normal document load, so the legacy initialization
 * runs once per page, just as it did in the original three-page website.
 */
@Component({
  selector: 'app-original-page',
  standalone: true,
  template: '',
})
export class OriginalPageComponent implements OnInit {
  @Input({ required: true }) page!: string;
  private readonly element = inject(ElementRef<HTMLElement>);
  private readonly http = inject(HttpClient);

  ngOnInit(): void {
    this.http.get(`/${this.page}.fragment.html`, { responseType: 'text' }).subscribe({
      next: (html) => {
        this.element.nativeElement.innerHTML = html;
        // Keep original navigation as document navigation (legacy JS lifecycle).
        // Initialize the original interactive controls after the DOM exists.
        // On each page load the script is evaluated only once.
        const script = document.createElement('script');
        script.src = '/js/main.js';
      script.dataset['furrowInit'] = 'true';
        document.body.appendChild(script);
      },
      error: (err) => console.error('Unable to load original page', err),
    });
  }
}
