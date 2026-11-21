# Furrow — Angular 21

Conversion conservatrice du template HTML/CSS/JS original. Les trois pages originales sont rendues par des composants Angular standalone; les fichiers CSS, images, Bootstrap et JavaScript d’origine sont conservés. Les fonctionnalités de démonstration restent front-end uniquement.

## Lancement

Prérequis : Node.js 20.19+ ou 22.12+ et npm.

```bash
npm install
npm start
```

Ouvrir http://localhost:4200/

Routes : `/` (accueil), `/shop` (boutique), `/visit` (visite).

## Architecture

- `src/app/pages/` : trois composants Angular standalone
- `src/app/app.routes.ts` : routes Angular
- `src/app/shared/original-page.component.ts` : adaptateur de compatibilité qui charge le HTML original sans changer sa structure DOM
- `public/*.fragment.html` : contenu HTML original des pages, avec seulement les liens internes ajustés
- `public/css/`, `public/img/`, `public/js/` : fichiers originaux inchangés

**Important :** pour conserver le rendu et les interactions du modèle d’origine, les liens de pages utilisent un chargement de document classique. Le HTML/JS d’origine est encapsulé dans Angular, mais les fonctionnalités JavaScript n’ont pas été réécrites en services Angular. Aucune API/backend n’a été ajoutée.
