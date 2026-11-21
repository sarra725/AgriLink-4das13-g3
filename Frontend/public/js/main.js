/* ==================================================================
 * Furrow — main.js
 * Free farm website template by uiCookies — https://uicookies.com/
 * Vanilla JavaScript only: one function per feature, guard clauses
 * for anything that is not on the current page.
 * ================================================================== */
(function () {
  'use strict';

  /* ----------------------------------------------------------------
   * Settings a farm will want to edit
   * ---------------------------------------------------------------- */
  var DELIVERY = {
    zones: ['DT9', 'DT10', 'DT11', 'SP7', 'SP8', 'BA8', 'BA9'], // outward postcodes you deliver to
    days: 'Tuesday and Friday',
    fee: 3.5,        // shop delivery fee
    freeOver: 45,    // free shop delivery at or above this basket value
    minimum: 20      // minimum basket for shop delivery
  };
  var CART_KEY = 'brookmoor-basket';

  /* Seasonal calendar: months a crop is available (1 = January) and
     the months it is at its best. `where` = shop, box and/or pick. */
  var PRODUCE = [
    { name: 'Asparagus', icon: 'stalk', months: [4, 5, 6], peak: [5], where: ['shop', 'box'] },
    { name: 'Rhubarb', icon: 'stalk', months: [2, 3, 4, 5, 6, 7], peak: [4, 5], where: ['shop', 'box', 'pick'] },
    { name: 'Purple sprouting broccoli', icon: 'leaf', months: [2, 3, 4], peak: [3], where: ['shop', 'box'] },
    { name: 'Wild garlic', icon: 'leaf', months: [3, 4, 5], peak: [4], where: ['shop'] },
    { name: 'Spring greens', icon: 'leaf', months: [3, 4, 5], peak: [4], where: ['shop', 'box'] },
    { name: 'Radishes', icon: 'root', months: [4, 5, 6, 7, 8, 9], peak: [5, 6], where: ['box'] },
    { name: 'Spinach', icon: 'leaf', months: [4, 5, 6, 9, 10], peak: [5], where: ['box'] },
    { name: 'Salad leaves', icon: 'leaf', months: [4, 5, 6, 7, 8, 9, 10], peak: [6, 7], where: ['shop', 'box'] },
    { name: 'New potatoes', icon: 'root', months: [5, 6, 7, 8], peak: [6, 7], where: ['shop', 'box'] },
    { name: 'Broad beans', icon: 'pod', months: [6, 7, 8], peak: [7], where: ['shop', 'box', 'pick'] },
    { name: 'Peas', icon: 'pod', months: [6, 7, 8], peak: [7], where: ['box', 'pick'] },
    { name: 'Strawberries', icon: 'berry', months: [6, 7, 8, 9], peak: [6, 7], where: ['shop', 'pick'] },
    { name: 'Gooseberries', icon: 'berry', months: [6, 7], peak: [7], where: ['pick'] },
    { name: 'Blackcurrants', icon: 'berry', months: [7, 8], peak: [7], where: ['pick'] },
    { name: 'Raspberries', icon: 'berry', months: [7, 8, 9, 10], peak: [7, 9], where: ['shop', 'pick'] },
    { name: 'Courgettes', icon: 'squash', months: [6, 7, 8, 9, 10], peak: [7, 8], where: ['shop', 'box'] },
    { name: 'Tomatoes', icon: 'tomato', months: [7, 8, 9, 10], peak: [8, 9], where: ['shop', 'box'] },
    { name: 'Runner beans', icon: 'pod', months: [7, 8, 9, 10], peak: [8], where: ['shop', 'box', 'pick'] },
    { name: 'Beetroot', icon: 'root', months: [6, 7, 8, 9, 10, 11], peak: [8, 9], where: ['shop', 'box'] },
    { name: 'Carrots', icon: 'root', months: [6, 7, 8, 9, 10, 11, 12, 1], peak: [9, 10], where: ['shop', 'box'] },
    { name: 'Sweetcorn', icon: 'corn', months: [8, 9, 10], peak: [9], where: ['shop', 'box', 'pick'] },
    { name: 'Plums', icon: 'apple', months: [8, 9], peak: [8], where: ['shop', 'pick'] },
    { name: 'Blackberries', icon: 'berry', months: [8, 9], peak: [9], where: ['pick'] },
    { name: 'Apples', icon: 'apple', months: [8, 9, 10, 11, 12, 1, 2, 3], peak: [9, 10], where: ['shop', 'box', 'pick'] },
    { name: 'Pears', icon: 'apple', months: [9, 10, 11], peak: [10], where: ['shop', 'box'] },
    { name: 'Onions', icon: 'allium', months: [8, 9, 10, 11, 12, 1, 2, 3], peak: [9], where: ['shop', 'box'] },
    { name: 'Squash and pumpkins', icon: 'squash', months: [9, 10, 11, 12, 1], peak: [10], where: ['shop', 'box', 'pick'] },
    { name: 'Leeks', icon: 'allium', months: [9, 10, 11, 12, 1, 2, 3, 4], peak: [11, 12, 1], where: ['shop', 'box'] },
    { name: 'Kale', icon: 'leaf', months: [9, 10, 11, 12, 1, 2, 3, 4], peak: [11, 12, 1, 2], where: ['shop', 'box'] },
    { name: 'Parsnips', icon: 'root', months: [10, 11, 12, 1, 2, 3], peak: [12, 1], where: ['shop', 'box'] },
    { name: 'Brussels sprouts', icon: 'leaf', months: [10, 11, 12, 1, 2], peak: [12], where: ['shop', 'box'] },
    { name: 'Celeriac', icon: 'root', months: [10, 11, 12, 1, 2, 3], peak: [11, 12], where: ['box'] },
    { name: 'Swede', icon: 'root', months: [10, 11, 12, 1, 2, 3], peak: [1], where: ['shop', 'box'] },
    { name: 'Savoy cabbage', icon: 'leaf', months: [11, 12, 1, 2, 3], peak: [1, 2], where: ['shop', 'box'] }
  ];

  var PICK_SEASON = [6, 7, 8, 9, 10]; // months the pick-your-own fields are open

  var MONTHS = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
  var DAYS = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
  var WHERE_LABEL = { shop: 'Shop', box: 'Box', pick: 'Pick your own' };
  var STATUS_LABEL = { peak: 'At its best', fresh: 'Just in', last: 'Last few weeks', on: 'In season' };

  var reduceMotion = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  var money = new Intl.NumberFormat('en-GB', { style: 'currency', currency: 'GBP' });

  /* ----------------------------------------------------------------
   * Small helpers
   * ---------------------------------------------------------------- */
  function $(sel, ctx) { return (ctx || document).querySelector(sel); }
  function $$(sel, ctx) { return Array.prototype.slice.call((ctx || document).querySelectorAll(sel)); }
  function esc(s) { return String(s).replace(/[&<>"']/g, function (c) { return { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]; }); }
  function icon(id) { return '<svg class="ico" aria-hidden="true"><use href="#i-' + id + '"/></svg>'; }

  function store(key, value) {
    try {
      if (value === undefined) return JSON.parse(window.localStorage.getItem(key) || 'null');
      window.localStorage.setItem(key, JSON.stringify(value));
    } catch (e) { /* storage blocked: the page still works, it just forgets */ }
    return null;
  }

  function prettyTime(mins) {
    var h = Math.floor(mins / 60), m = mins % 60;
    var suffix = h >= 12 ? 'pm' : 'am';
    var h12 = h % 12 || 12;
    return h12 + (m ? ':' + (m < 10 ? '0' : '') + m : '') + suffix;
  }
  function toMins(hhmm) {
    if (!hhmm) return null;
    var p = hhmm.split(':');
    return parseInt(p[0], 10) * 60 + parseInt(p[1] || '0', 10);
  }
  function addDays(date, n) { var d = new Date(date); d.setDate(d.getDate() + n); return d; }
  function longDate(date) { return DAYS[date.getDay()] + ' ' + date.getDate() + ' ' + MONTHS[date.getMonth()]; }

  /* Current time on the farm, whatever the visitor's own time zone. */
  function farmNow(tz) {
    var now = new Date();
    try {
      var parts = {};
      new Intl.DateTimeFormat('en-GB', {
        timeZone: tz || 'Europe/London', weekday: 'short', hour: '2-digit', minute: '2-digit', hourCycle: 'h23'
      }).formatToParts(now).forEach(function (p) { parts[p.type] = p.value; });
      var day = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].indexOf(parts.weekday);
      return { day: day, mins: parseInt(parts.hour, 10) * 60 + parseInt(parts.minute, 10) };
    } catch (e) {
      return { day: now.getDay(), mins: now.getHours() * 60 + now.getMinutes() };
    }
  }

  /* Farm shop hours come from the footer list, so there is one place to edit them. */
  function readHours() {
    var list = $('[data-hours]');
    if (!list) return null;
    var hours = {};
    $$('[data-day]', list).forEach(function (li) {
      hours[li.getAttribute('data-day')] = { open: toMins(li.getAttribute('data-open')), close: toMins(li.getAttribute('data-close')) };
    });
    return { tz: list.getAttribute('data-tz'), days: hours };
  }

  /* ----------------------------------------------------------------
   * Navigation: close the mobile menu after a tap, sticky shadow, spy
   * ---------------------------------------------------------------- */
  function initNav() {
    var toggler = $('.navbar-toggler');
    var menu = $('#main-nav');
    if (!toggler || !menu) return;
    menu.addEventListener('show.bs.collapse', function () { toggler.setAttribute('aria-label', 'Close menu'); });
    menu.addEventListener('hide.bs.collapse', function () { toggler.setAttribute('aria-label', 'Open menu'); });
    $$('a', menu).forEach(function (link) {
      link.addEventListener('click', function () {
        if (!menu.classList.contains('show') || !window.bootstrap) return;
        window.bootstrap.Collapse.getOrCreateInstance(menu).hide();
      });
    });
  }

  function initStickyHeader() {
    var header = $('[data-sticky-header]');
    if (!header) return;
    var onScroll = function () { header.classList.toggle('is-scrolled', window.scrollY > 24); };
    onScroll();
    window.addEventListener('scroll', onScroll, { passive: true });
  }

  function initScrollSpy() {
    var links = $$('.navbar .nav-link[data-spy]').filter(function (l) { return l.getAttribute('href').charAt(0) === '#'; });
    if (!links.length || !('IntersectionObserver' in window)) return;
    var byId = {};
    links.forEach(function (l) { byId[l.getAttribute('data-spy')] = l; });
    var io = new IntersectionObserver(function (entries) {
      entries.forEach(function (entry) {
        if (!entry.isIntersecting) return;
        links.forEach(function (l) { l.classList.remove('active'); l.removeAttribute('aria-current'); });
        var link = byId[entry.target.id];
        if (link) { link.classList.add('active'); link.setAttribute('aria-current', 'true'); }
      });
    }, { rootMargin: '-45% 0px -50% 0px' });
    Object.keys(byId).forEach(function (id) { var s = document.getElementById(id); if (s) io.observe(s); });
  }

  /* ----------------------------------------------------------------
   * Open / closed status from the hours list, today's row highlighted
   * ---------------------------------------------------------------- */
  function initOpenStatus() {
    var hours = readHours();
    var targets = $$('[data-open-status]');
    if (!hours) return;
    var now = farmNow(hours.tz);
    var today = hours.days[now.day] || {};
    var open = today.open !== null && today.open !== undefined && now.mins >= today.open && now.mins < today.close;
    var text;

    if (open) {
      text = (today.close - now.mins <= 45 ? 'Closing soon' : 'Open now') + ' · farm shop until ' + prettyTime(today.close);
    } else if (today.open !== null && today.open !== undefined && now.mins < today.open) {
      text = 'Closed · opens today at ' + prettyTime(today.open);
    } else {
      for (var i = 1; i <= 7; i++) {
        var d = (now.day + i) % 7, h = hours.days[d];
        if (h && h.open !== null) {
          text = 'Closed · opens ' + (i === 1 ? 'tomorrow' : DAYS[d]) + ' at ' + prettyTime(h.open);
          break;
        }
      }
    }

    targets.forEach(function (el) {
      var t = $('.status-text', el);
      if (t && text) t.textContent = text;
      el.classList.toggle('is-open', open);
      el.classList.toggle('is-closed', !open);
    });
    $$('[data-hours-row], [data-hours] [data-day]').forEach(function (row) {
      var isToday = row.getAttribute('data-day') === String(now.day);
      row.classList.toggle('is-today', isToday);
      if (isToday && row.matches('[data-hours-row]')) {
        var th = $('th', row);
        if (th && !$('.today-tag', th)) th.insertAdjacentHTML('beforeend', ' <span class="today-tag">Today</span>');
      }
    });
  }

  /* "Week of Monday 21 September" wherever a week label is shown. */
  function initWeekLabel() {
    var labels = $$('[data-week-label]');
    if (!labels.length) return;
    var now = new Date();
    var monday = addDays(now, -((now.getDay() + 6) % 7));
    labels.forEach(function (el) { el.textContent = 'Week of ' + longDate(monday); });
  }

  /* ----------------------------------------------------------------
   * In season now: month picker + produce list built from PRODUCE
   * ---------------------------------------------------------------- */
  function seasonOf(month) {
    if (month >= 3 && month <= 5) return 'spring';
    if (month >= 6 && month <= 8) return 'summer';
    if (month >= 9 && month <= 11) return 'autumn';
    return 'winter';
  }
  function prevMonth(m) { return m === 1 ? 12 : m - 1; }
  function nextMonth(m) { return m === 12 ? 1 : m + 1; }

  function statusFor(item, m) {
    if (item.peak.indexOf(m) > -1) return 'peak';
    if (item.months.indexOf(prevMonth(m)) === -1) return 'fresh';
    if (item.months.indexOf(nextMonth(m)) === -1) return 'last';
    return 'on';
  }

  /* Human description of a crop's season for screen readers. */
  function describeRuns(months) {
    if (months.length === 12) return 'all year';
    var runs = [];
    months.forEach(function (m) {
      if (months.indexOf(prevMonth(m)) > -1) return; // not the start of a run
      var end = m;
      while (months.indexOf(nextMonth(end)) > -1) end = nextMonth(end);
      runs.push(m === end ? MONTHS[m - 1] : MONTHS[m - 1] + ' to ' + MONTHS[end - 1]);
    });
    return runs.join(' and ');
  }

  function cropsFor(m) {
    var order = { peak: 0, fresh: 1, on: 2, last: 3 };
    return PRODUCE.filter(function (p) { return p.months.indexOf(m) > -1; })
      .map(function (p) { return { item: p, status: statusFor(p, m) }; })
      .sort(function (a, b) { return order[a.status] - order[b.status] || a.item.name.localeCompare(b.item.name); });
  }

  function produceCard(entry, m) {
    var p = entry.item;
    var cells = '';
    for (var i = 1; i <= 12; i++) {
      var cls = [];
      if (p.months.indexOf(i) > -1) cls.push('on');
      if (p.peak.indexOf(i) > -1) cls.push('peak');
      if (i === m) cls.push('now');
      cells += '<i class="' + cls.join(' ') + '" data-m="' + MONTHS[i - 1].charAt(0) + '"></i>';
    }
    var best = p.peak.map(function (x) { return MONTHS[x - 1]; }).join(' and ');
    var label = 'In season ' + describeRuns(p.months) + ', best in ' + best;
    var where = whereFor(p, m).map(function (w) { return '<li class="where-' + w + '">' + WHERE_LABEL[w] + '</li>'; }).join('');
    return '<li class="produce-card status-' + entry.status + '">' +
      '<div class="produce-top"><span class="produce-icon">' + icon(p.icon) + '</span>' +
      '<div><h3 class="produce-name">' + esc(p.name) + '</h3><p class="produce-status">' + STATUS_LABEL[entry.status] + '</p></div></div>' +
      '<span class="season-strip" role="img" aria-label="' + esc(label) + '">' + cells + '</span>' +
      '<ul class="produce-where" aria-label="Where to get it">' + where + '</ul></li>';
  }

  function whereFor(p, m) {
    return p.where.filter(function (w) { return w !== 'pick' || PICK_SEASON.indexOf(m) > -1; });
  }

  function seasonSummary(m) {
    var list = cropsFor(m);
    var peak = list.filter(function (e) { return e.status === 'peak'; }).length;
    var pick = list.filter(function (e) { return whereFor(e.item, m).indexOf('pick') > -1; }).length;
    return list.length + ' crops in season in ' + MONTHS[m - 1] + ': ' + peak + ' at their best' +
      (pick ? ', ' + pick + ' to pick yourself.' : '. The pick-your-own fields open again in June.');
  }

  function initSeason() {
    var grid = $('[data-produce-grid]');
    var picker = $('[data-month-picker]');
    var current = new Date().getMonth() + 1;
    var hero = $('[data-hero]');

    // Seasonal hero: season name, accent and "best this month" list.
    if (hero) {
      hero.setAttribute('data-season', seasonOf(current));
      var name = $('[data-season-name]', hero);
      if (name) name.textContent = seasonOf(current).charAt(0).toUpperCase() + seasonOf(current).slice(1);
      var picking = $('[data-hero-picking]', hero);
      if (picking) {
        var best = cropsFor(current).filter(function (e) { return e.status === 'peak'; }).slice(0, 5);
        if (best.length < 3) best = cropsFor(current).slice(0, 5);
        picking.innerHTML = best.map(function (e) { return '<li>' + esc(e.item.name) + '</li>'; }).join('');
      }
    }

    if (!grid || !picker) return;
    var buttons = $$('.month-btn', picker);
    var title = $('[data-month-title]');
    var summary = $('[data-season-summary]');
    var month = current;

    function render(m, announce) {
      month = m;
      grid.innerHTML = cropsFor(m).map(function (e) { return produceCard(e, m); }).join('');
      buttons.forEach(function (b) {
        var on = b.getAttribute('data-month') === String(m);
        b.setAttribute('aria-pressed', on ? 'true' : 'false');
        b.classList.toggle('is-current', b.getAttribute('data-month') === String(current));
      });
      if (title) title.textContent = MONTHS[m - 1];
      if (summary) {
        summary.textContent = seasonSummary(m);
        summary.setAttribute('aria-live', announce ? 'polite' : 'off');
      }
      var active = buttons[m - 1];
      if (active && active.scrollIntoView && announce) {
        var list = active.parentNode;
        list.scrollTo({ left: active.offsetLeft - list.clientWidth / 2 + active.clientWidth / 2, behavior: reduceMotion ? 'auto' : 'smooth' });
      }
    }

    buttons.forEach(function (b, i) {
      b.addEventListener('click', function () { render(parseInt(b.getAttribute('data-month'), 10), true); });
      b.addEventListener('keydown', function (e) {
        var to = null;
        if (e.key === 'ArrowRight') to = (i + 1) % 12;
        if (e.key === 'ArrowLeft') to = (i + 11) % 12;
        if (e.key === 'Home') to = 0;
        if (e.key === 'End') to = 11;
        if (to === null) return;
        e.preventDefault();
        buttons[to].focus();
        render(to + 1, true);
      });
    });
    $$('[data-month-step]', picker).forEach(function (b) {
      b.addEventListener('click', function () {
        var step = parseInt(b.getAttribute('data-month-step'), 10);
        render(step > 0 ? nextMonth(month) : prevMonth(month), true);
      });
    });
    render(current, false);
  }

  /* ----------------------------------------------------------------
   * Form validation shared by every form
   * ---------------------------------------------------------------- */
  function parsePostcode(value) {
    var v = String(value || '').toUpperCase().replace(/\s+/g, ' ').trim();
    var full = v.match(/^([A-Z]{1,2}\d[A-Z\d]?)\s?(\d[A-Z]{2})$/);
    var outward = full ? full[1] : (v.match(/^[A-Z]{1,2}\d[A-Z\d]?$/) ? v : null);
    if (!outward) return { valid: false };
    return { valid: true, outward: outward, pretty: full ? full[1] + ' ' + full[2] : outward, inZone: DELIVERY.zones.indexOf(outward) > -1 };
  }

  function fieldError(input) {
    var id = (input.getAttribute('aria-describedby') || '').split(' ')[0];
    return id ? document.getElementById(id) : null;
  }

  function checkField(input) {
    var v = input.value.trim();
    var ok = true;
    if (input.hasAttribute('data-required') && !v) ok = false;
    var type = input.getAttribute('data-type');
    if (ok && v && type === 'email') ok = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(v);
    if (ok && v && type === 'postcode') { var pc = parsePostcode(v); ok = pc.valid && pc.inZone; }
    setInvalid(input, !ok);
    return ok;
  }

  function setInvalid(input, bad) {
    input.classList.toggle('is-invalid', bad);
    if (bad) input.setAttribute('aria-invalid', 'true'); else input.removeAttribute('aria-invalid');
    var err = fieldError(input);
    if (err) err.classList.toggle('is-visible', bad);
  }

  function checkForm(form) {
    var fields = $$('[data-required], [data-type]', form);
    var firstBad = null;
    fields.forEach(function (f) { if (!checkField(f) && !firstBad) firstBad = f; });
    if (firstBad) firstBad.focus();
    return !firstBad;
  }

  function watchFields(form) {
    $$('[data-required], [data-type]', form).forEach(function (f) {
      f.addEventListener('blur', function () { if (f.value.trim()) checkField(f); });
      f.addEventListener('input', function () { if (f.classList.contains('is-invalid')) checkField(f); });
    });
  }

  function showSuccess(form, html, hideSel) {
    var box = $('[data-success]', form);
    if (!box) return;
    if (hideSel) $$(hideSel, form).forEach(function (el) { el.hidden = true; });
    box.innerHTML = html;
    box.hidden = false;
    box.setAttribute('tabindex', '-1');
    box.focus();
  }

  function initForms() {
    $$('form[data-validate]').forEach(function (form) {
      watchFields(form);
      form.addEventListener('submit', function (e) {
        e.preventDefault();
        if (!checkForm(form)) return;
        var email = $('input[type=email]', form);
        showSuccess(form,
          icon('check') + '<div><strong>You&rsquo;re on the list.</strong> The next bulletin goes to <span class="nowrap">' + esc(email ? email.value.trim() : 'you') + '</span> on Thursday morning.</div>',
          '.news-fields');
      });
    });
  }

  /* ----------------------------------------------------------------
   * Veg box builder: tier, frequency and add-ons -> live price
   * ---------------------------------------------------------------- */
  function firstBoxDate() {
    // Orders close Monday midnight for that Thursday's round.
    var now = new Date();
    var day = now.getDay();
    var n = (4 - day + 7) % 7;
    if (day >= 2 && day <= 4) n += 7;
    return addDays(now, n);
  }

  function initBoxBuilder() {
    var form = $('[data-box-builder]');
    if (!form) return;
    var lines = $('[data-box-lines]', form);
    var total = $('[data-box-total]', form);
    var meta = $('[data-box-meta]', form);
    var first = $('[data-first-box]', form);
    var firstDate = firstBoxDate();
    if (first) first.textContent = longDate(firstDate);

    function state() {
      var freq = ($('input[name=freq]:checked', form) || {}).value || 'weekly';
      var tier = $('input[name=tier]:checked', form) || $('input[name=tier]', form);
      var addons = $$('input[name=addon]:checked', form);
      return { freq: freq, tier: tier, addons: addons };
    }

    function update() {
      var s = state();
      $$('input[name=tier]', form).forEach(function (t) {
        var amount = $('[data-tier-price]', t.parentNode);
        if (amount) amount.textContent = money.format(parseFloat(t.getAttribute('data-' + s.freq)));
      });
      var base = parseFloat(s.tier.getAttribute('data-' + s.freq));
      var sum = base;
      var html = '<li><span>' + esc(s.tier.getAttribute('data-name')) + ', ' + (s.freq === 'weekly' ? 'every week' : 'every other week') + '</span><span>' + money.format(base) + '</span></li>';
      s.addons.forEach(function (a) {
        var p = parseFloat(a.getAttribute('data-price'));
        sum += p;
        html += '<li><span>' + esc(a.getAttribute('data-name')) + '</span><span>+' + money.format(p) + '</span></li>';
      });
      lines.innerHTML = html;
      total.textContent = money.format(sum);
      var perMonth = sum * (s.freq === 'weekly' ? 52 : 26) / 12;
      meta.textContent = 'About ' + money.format(perMonth) + ' a month, ' + (s.freq === 'weekly' ? '52' : '26') + ' boxes a year';
      form.setAttribute('data-total', sum.toFixed(2));
    }

    form.addEventListener('change', update);
    watchFields(form);
    form.addEventListener('submit', function (e) {
      e.preventDefault();
      if (!checkForm(form)) return;
      var s = state();
      var pc = parsePostcode($('#box-postcode', form).value);
      showSuccess(form,
        icon('check') + '<div><strong>Your ' + esc(s.tier.getAttribute('data-name').toLowerCase()) + ' is set up.</strong> ' +
        (s.freq === 'weekly' ? 'Every week' : 'Every other week') + ' at ' + total.textContent + ' a box, first delivery ' +
        esc(longDate(firstDate)) + ' to ' + esc(pc.pretty) + '. You can skip or change it any time before Monday midnight.</div>',
        '[data-box-fields]');
    });
    update();
  }

  /* ----------------------------------------------------------------
   * Event booking modal (Bootstrap modal, populated from the button)
   * ---------------------------------------------------------------- */
  function upcomingWeekdays(days, count) {
    var out = [];
    var d = addDays(new Date(), 1);
    while (out.length < count) {
      if (days.indexOf(d.getDay()) > -1) out.push(longDate(d) + ', 10:30am');
      d = addDays(d, 1);
    }
    return out;
  }

  function initBooking() {
    var modal = $('#book-modal');
    if (!modal) return;
    var form = $('[data-booking]', modal);
    var title = $('[data-book-title]', modal);
    var dates = $('[data-book-dates]', modal);
    var totalEl = $('[data-book-total]', modal);
    var ticketErr = $('[data-ticket-error]', modal);
    var rows = { adult: $('[data-ticket="adult"]', modal), child: $('[data-ticket="child"]', modal) };
    var prices = { adult: 0, child: 0 };
    watchFields(form);

    function qty(kind) { var i = $('input', rows[kind]); return rows[kind].hidden ? 0 : Math.max(0, parseInt(i.value, 10) || 0); }
    function update() {
      var t = qty('adult') * prices.adult + qty('child') * prices.child;
      totalEl.textContent = money.format(t);
      if (qty('adult') + qty('child') > 0) ticketErr.classList.remove('is-visible');
    }

    $$('.stepper', modal).forEach(function (st) {
      var input = $('input', st);
      $$('[data-step]', st).forEach(function (b) {
        b.addEventListener('click', function () {
          var v = (parseInt(input.value, 10) || 0) + parseInt(b.getAttribute('data-step'), 10);
          input.value = Math.min(parseInt(input.max, 10), Math.max(0, v));
          update();
        });
      });
      input.addEventListener('input', update);
    });

    modal.addEventListener('show.bs.modal', function (e) {
      var btn = e.relatedTarget;
      if (!btn) return;
      form.reset();
      $$('[data-book-fields], [data-book-foot]', modal).forEach(function (el) { el.hidden = false; });
      $('[data-success]', modal).hidden = true;
      $$('.is-invalid', form).forEach(function (f) { setInvalid(f, false); });
      ticketErr.classList.remove('is-visible');

      title.textContent = btn.getAttribute('data-event');
      prices.adult = parseFloat(btn.getAttribute('data-adult')) || 0;
      prices.child = parseFloat(btn.getAttribute('data-child')) || 0;
      rows.child.hidden = !btn.getAttribute('data-child');
      $('[data-ticket-price]', rows.adult).textContent = money.format(prices.adult) + ' each';
      $('[data-ticket-price]', rows.child).textContent = money.format(prices.child) + ' each';
      $('.ticket-label', rows.adult).textContent = rows.child.hidden ? 'Places' : 'Adults';

      var list = btn.getAttribute('data-recurring')
        ? upcomingWeekdays(btn.getAttribute('data-recurring').split(',').map(Number), 8)
        : (btn.getAttribute('data-dates') || '').split('|');
      dates.innerHTML = list.map(function (d) { return '<option>' + esc(d) + '</option>'; }).join('');
      update();
    });

    form.addEventListener('submit', function (e) {
      e.preventDefault();
      var tickets = qty('adult') + qty('child');
      var ok = checkForm(form);
      if (!tickets) { ticketErr.classList.add('is-visible'); ok = false; }
      if (!ok) return;
      var who = qty('adult') + (rows.child.hidden ? (qty('adult') === 1 ? ' place' : ' places') : (qty('adult') === 1 ? ' adult' : ' adults'));
      if (!rows.child.hidden && qty('child')) who += ' and ' + qty('child') + (qty('child') === 1 ? ' child' : ' children');
      showSuccess(form,
        icon('check') + '<div><strong>See you on the farm.</strong> ' + esc(title.textContent) + ', ' + esc(dates.value) + ': ' + who +
        ', ' + totalEl.textContent + ' to pay when you arrive. Park in the yard and follow the chalk arrows.</div>' +
        '<button type="button" class="btn btn-outline-green btn-sm" data-bs-dismiss="modal">Close</button>',
        '[data-book-fields], [data-book-foot]');
    });
  }

  /* ----------------------------------------------------------------
   * Count-up numbers when they scroll into view
   * ---------------------------------------------------------------- */
  function initCounters() {
    var nums = $$('[data-count]');
    if (!nums.length || reduceMotion || !('IntersectionObserver' in window)) return;
    var io = new IntersectionObserver(function (entries) {
      entries.forEach(function (entry) {
        if (!entry.isIntersecting) return;
        io.unobserve(entry.target);
        var el = entry.target, end = parseInt(el.getAttribute('data-count'), 10), suffix = el.getAttribute('data-suffix') || '';
        var start = performance.now(), dur = 700;
        (function tick(t) {
          var k = Math.min(1, (t - start) / dur);
          el.textContent = Math.round(end * (1 - Math.pow(1 - k, 3))) + suffix;
          if (k < 1) requestAnimationFrame(tick);
        })(start);
      });
    }, { threshold: 0.6 });
    nums.forEach(function (n) { io.observe(n); });
  }

  /* Smooth scrolling for in-page links (kept out of CSS so scripted scrolls stay instant). */
  function initSmoothAnchors() {
    document.addEventListener('click', function (e) {
      var link = e.target.closest('a[href^="#"]');
      if (!link || link.getAttribute('href').length < 2 || link.hasAttribute('data-bs-toggle')) return;
      var target = document.getElementById(link.getAttribute('href').slice(1));
      if (!target) return;
      e.preventDefault();
      target.scrollIntoView({ behavior: reduceMotion ? 'auto' : 'smooth', block: 'start' });
      history.pushState(null, '', link.getAttribute('href'));
      if (!target.hasAttribute('tabindex') && !/^(A|BUTTON|INPUT|SELECT|TEXTAREA)$/.test(target.tagName)) target.setAttribute('tabindex', '-1');
      target.focus({ preventScroll: true });
    });
  }

  /* Gentle entrance as sections scroll into view. Nothing is hidden before
     it animates, so content is always visible without JS or IntersectionObserver. */
  function initReveal() {
    if (reduceMotion || !('IntersectionObserver' in window)) return;
    var els = $$('.section-head, .produce-grid, .tier-grid, .box-lower, .pick-board, .story-media, .timeline, .stats-row, .event-card, .event-list, .news-card, .hours-photo, .product-grid, .guide-steps, .map-wrap, .info-card, .faq-list');
    els.forEach(function (el) { el.classList.add('reveal'); });
    var io = new IntersectionObserver(function (entries) {
      entries.forEach(function (entry) {
        if (entry.isIntersecting) { entry.target.classList.add('is-in'); io.unobserve(entry.target); }
      });
    }, { rootMargin: '0px 0px -8% 0px', threshold: 0.05 });
    els.forEach(function (el) { io.observe(el); });
  }

  /* ----------------------------------------------------------------
   * Basket (localStorage) — header count on every page
   * ---------------------------------------------------------------- */
  function readCart() {
    var c = store(CART_KEY) || {};
    return { items: Array.isArray(c.items) ? c.items : [], mode: c.mode || 'collect', postcode: c.postcode || '', slot: c.slot || '' };
  }
  function saveCart(c) { store(CART_KEY, c); updateCartBadges(c); }
  function cartCount(c) { return c.items.reduce(function (n, i) { return n + i.qty; }, 0); }
  function cartSubtotal(c) { return c.items.reduce(function (n, i) { return n + i.qty * i.price; }, 0); }

  function updateCartBadges(c) {
    var n = cartCount(c || readCart());
    $$('[data-cart-count]').forEach(function (el) { el.textContent = n; el.classList.toggle('is-empty', !n); });
    $$('[data-cart-link]').forEach(function (el) {
      el.setAttribute('aria-label', (el.tagName === 'BUTTON' ? 'Open basket, ' : 'Basket, ') + n + (n === 1 ? ' item' : ' items'));
    });
  }

  /* ----------------------------------------------------------------
   * Shop: filters, sort, add to basket
   * ---------------------------------------------------------------- */
  function initShopFilters() {
    var grid = $('[data-product-grid]');
    var chips = $$('[data-filter]');
    var sort = $('[data-sort]');
    var count = $('[data-result-count]');
    if (!grid) return;
    var cards = $$('[data-product]', grid);
    cards.forEach(function (c, i) { c.setAttribute('data-order', i); });
    var active = 'all';

    chips.forEach(function (chip) {
      var cat = chip.getAttribute('data-filter');
      var n = cat === 'all' ? cards.length : cards.filter(function (c) { return c.getAttribute('data-cat') === cat; }).length;
      var badge = $('.chip-count', chip);
      if (badge) badge.textContent = n;
    });

    function apply() {
      var shown = 0;
      cards.forEach(function (c) {
        var show = active === 'all' || c.getAttribute('data-cat') === active;
        c.hidden = !show;
        if (show) shown++;
      });
      var how = sort ? sort.value : 'featured';
      var sorted = cards.slice().sort(function (a, b) {
        var pa = parseFloat($('[data-add]', a).getAttribute('data-price')), pb = parseFloat($('[data-add]', b).getAttribute('data-price'));
        if (how === 'low') return pa - pb;
        if (how === 'high') return pb - pa;
        if (how === 'name') return $('.product-name', a).textContent.localeCompare($('.product-name', b).textContent);
        return a.getAttribute('data-order') - b.getAttribute('data-order');
      });
      sorted.forEach(function (c) { grid.appendChild(c); });
      if (count) count.textContent = 'Showing ' + shown + (shown === 1 ? ' product' : ' products');
    }

    chips.forEach(function (chip) {
      chip.addEventListener('click', function () {
        active = chip.getAttribute('data-filter');
        chips.forEach(function (c) { c.setAttribute('aria-pressed', c === chip ? 'true' : 'false'); });
        apply();
      });
    });
    if (sort) sort.addEventListener('change', apply);
    apply();
  }

  /* ----------------------------------------------------------------
   * Shop: basket drawer, collection/delivery, postcode check
   * ---------------------------------------------------------------- */
  function collectionSlots() {
    var hours = readHours();
    var out = [];
    if (!hours) return out;
    var d = addDays(new Date(), 1);
    while (out.length < 6) {
      var h = hours.days[d.getDay()];
      if (h && h.open !== null) out.push(longDate(d) + ', ' + prettyTime(Math.max(h.open, 10 * 60)) + '–' + prettyTime(h.close));
      d = addDays(d, 1);
    }
    return out;
  }

  function postcodeMessage(value) {
    var pc = parsePostcode(value);
    if (!value.trim()) return { ok: false, cls: 'is-bad', html: 'Enter your postcode to check delivery.' };
    if (!pc.valid) return { ok: false, cls: 'is-bad', html: 'That doesn&rsquo;t look like a UK postcode. Try something like DT10 4PQ.' };
    if (!pc.inZone) return { ok: false, cls: 'is-bad', html: 'Sorry, we don&rsquo;t deliver to ' + esc(pc.outward) + ' yet. Collection from the farm shop is free.' };
    return { ok: true, cls: 'is-good', pc: pc, html: 'Good news: we deliver to ' + esc(pc.pretty) + ' every ' + DELIVERY.days + '.' };
  }

  function initPostcodeChecker() {
    $$('[data-postcode-check]').forEach(function (form) {
      var input = $('input', form);
      var out = $('[data-postcode-result]', form);
      form.addEventListener('submit', function (e) {
        e.preventDefault();
        var r = postcodeMessage(input.value);
        out.className = 'postcode-result ' + r.cls;
        out.innerHTML = icon(r.ok ? 'check' : 'info') + '<span>' + r.html + '</span>';
        if (r.ok) { var c = readCart(); c.postcode = r.pc.pretty; saveCart(c); }
      });
    });
  }

  function initCart() {
    updateCartBadges();
    var drawer = $('#basket');
    if (!drawer) return;
    var list = $('[data-cart-items]', drawer);
    var empty = $('[data-cart-empty]', drawer);
    var body = $('[data-cart-body]', drawer);
    var sub = $('[data-cart-subtotal]', drawer);
    var fee = $('[data-cart-fee]', drawer);
    var tot = $('[data-cart-total]', drawer);
    var slotSel = $('[data-collect-slot]', drawer);
    var pcInput = $('[data-deliver-postcode]', drawer);
    var pcOut = $('[data-deliver-result]', drawer);
    var meter = $('[data-free-meter]', drawer);
    var checkout = $('[data-checkout]', drawer);
    var checkoutErr = $('[data-checkout-error]', drawer);
    var success = $('[data-cart-success]', drawer);
    var live = $('[data-cart-live]');

    slotSel.innerHTML = collectionSlots().map(function (s) { return '<option>' + esc(s) + '</option>'; }).join('');

    function deliveryFee(c) {
      if (c.mode !== 'deliver') return 0;
      return cartSubtotal(c) >= DELIVERY.freeOver ? 0 : DELIVERY.fee;
    }

    function render() {
      var c = readCart();
      var n = cartCount(c);
      empty.hidden = n > 0;
      body.hidden = n === 0;
      list.innerHTML = c.items.map(function (i) {
        return '<li class="cart-item" data-id="' + esc(i.id) + '">' +
          (i.img ? '<img src="' + esc(i.img) + '" alt="" width="72" height="58">' : '<span class="cart-thumb" aria-hidden="true">' + icon('jar') + '</span>') +
          '<div class="cart-item-body"><p class="cart-item-name">' + esc(i.name) + '</p><p class="cart-item-unit">' + esc(i.unit) + ' &middot; ' + money.format(i.price) + '</p>' +
          '<div class="stepper stepper-sm"><button type="button" class="stepper-btn" data-qty="-1" aria-label="One fewer ' + esc(i.name) + '">' + icon('minus') + '</button>' +
          '<span class="stepper-val" aria-live="polite">' + i.qty + '</span>' +
          '<button type="button" class="stepper-btn" data-qty="1" aria-label="One more ' + esc(i.name) + '">' + icon('plus') + '</button></div></div>' +
          '<div class="cart-item-end"><p class="cart-line">' + money.format(i.qty * i.price) + '</p>' +
          '<button type="button" class="cart-remove" data-remove aria-label="Remove ' + esc(i.name) + '">' + icon('trash') + '</button></div></li>';
      }).join('');

      $$('input[name=fulfil]', drawer).forEach(function (r) { r.checked = r.value === c.mode; });
      drawer.setAttribute('data-mode', c.mode);
      if (pcInput && !pcInput.value && c.postcode) pcInput.value = c.postcode;
      if (c.slot) slotSel.value = c.slot;

      checkoutErr.textContent = '';
      checkoutErr.classList.remove('is-visible');
      var subtotal = cartSubtotal(c), f = deliveryFee(c);
      sub.textContent = money.format(subtotal);
      fee.textContent = c.mode === 'deliver' ? (f ? money.format(f) : 'Free') : 'Free';
      tot.textContent = money.format(subtotal + f);

      if (meter) {
        var bar = $('.meter-fill', meter), note = $('.meter-note', meter);
        var target = subtotal < DELIVERY.minimum ? DELIVERY.minimum : DELIVERY.freeOver;
        bar.style.width = Math.min(100, subtotal / DELIVERY.freeOver * 100) + '%';
        note.innerHTML = subtotal < DELIVERY.minimum
          ? 'Add ' + money.format(target - subtotal) + ' more for local delivery (minimum ' + money.format(DELIVERY.minimum) + ').'
          : subtotal < DELIVERY.freeOver
            ? money.format(target - subtotal) + ' away from free delivery.'
            : 'Free delivery unlocked.';
      }
    }

    function change(id, delta, remove) {
      var c = readCart();
      c.items = c.items.map(function (i) { if (i.id === id) i.qty += delta; return i; })
        .filter(function (i) { return i.qty > 0 && !(remove && i.id === id); });
      saveCart(c);
      render();
    }

    list.addEventListener('click', function (e) {
      var item = e.target.closest('.cart-item');
      if (!item) return;
      var id = item.getAttribute('data-id');
      var q = e.target.closest('[data-qty]');
      if (q) change(id, parseInt(q.getAttribute('data-qty'), 10));
      if (e.target.closest('[data-remove]')) {
        change(id, 0, true);
        var closeBtn = $('.btn-close', drawer);
        if (closeBtn) closeBtn.focus();
      }
    });

    $$('input[name=fulfil]', drawer).forEach(function (r) {
      r.addEventListener('change', function () { var c = readCart(); c.mode = r.value; saveCart(c); render(); });
    });
    slotSel.addEventListener('change', function () { var c = readCart(); c.slot = slotSel.value; saveCart(c); });

    var pcForm = $('[data-deliver-check]', drawer);
    if (pcForm) {
      $('button', pcForm).addEventListener('click', function () {
        var r = postcodeMessage(pcInput.value);
        pcOut.className = 'postcode-result ' + r.cls;
        pcOut.innerHTML = icon(r.ok ? 'check' : 'info') + '<span>' + r.html + '</span>';
        var c = readCart(); c.postcode = r.ok ? r.pc.pretty : ''; saveCart(c);
      });
      pcInput.addEventListener('keydown', function (e) { if (e.key === 'Enter') { e.preventDefault(); $('button', pcForm).click(); } });
    }

    // Add to basket buttons on product cards.
    $$('[data-add]').forEach(function (btn) {
      btn.addEventListener('click', function () {
        var c = readCart();
        var id = btn.getAttribute('data-add');
        var found = c.items.filter(function (i) { return i.id === id; })[0];
        if (found) found.qty += 1;
        else c.items.push({ id: id, name: btn.getAttribute('data-name'), price: parseFloat(btn.getAttribute('data-price')), unit: btn.getAttribute('data-unit'), img: btn.getAttribute('data-img') || '', qty: 1 });
        saveCart(c);
        render();
        btn.classList.add('is-added');
        var label = $('.add-label', btn);
        if (label) label.textContent = 'Added';
        if (live) live.textContent = btn.getAttribute('data-name') + ' added to your basket. ' + cartCount(c) + ' items in total.';
        clearTimeout(btn._t);
        btn._t = setTimeout(function () { btn.classList.remove('is-added'); if (label) label.textContent = 'Add'; }, 1600);
      });
    });

    checkout.addEventListener('click', function () {
      var c = readCart();
      var msg = '';
      if (c.mode === 'deliver') {
        var r = postcodeMessage(pcInput.value);
        if (!r.ok) msg = 'Check a postcode in our delivery area first, or switch to collection.';
        else if (cartSubtotal(c) < DELIVERY.minimum) msg = 'Local delivery needs a basket of at least ' + money.format(DELIVERY.minimum) + '.';
      }
      checkoutErr.textContent = msg;
      checkoutErr.classList.toggle('is-visible', !!msg);
      if (msg) return;
      var when = c.mode === 'deliver'
        ? 'Delivery to ' + esc(parsePostcode(pcInput.value).pretty) + ' on our next ' + DELIVERY.days + ' round'
        : 'Collection from the farm shop, ' + esc(slotSel.value);
      success.innerHTML = '<span class="success-badge">' + icon('check') + '</span><h3>Thank you, your order is in</h3>' +
        '<p>' + cartCount(c) + ' items, ' + tot.textContent + '. ' + when + '. Pay by card or cash when your order is handed over.</p>' +
        '<button type="button" class="btn btn-green" data-bs-dismiss="offcanvas">Back to the shop</button>';
      success.hidden = false;
      body.hidden = true;
      c.items = [];
      saveCart(c);
      success.setAttribute('tabindex', '-1');
      success.focus();
    });

    drawer.addEventListener('hidden.bs.offcanvas', function () {
      if (!success.hidden) { success.hidden = true; render(); }
    });

    if (location.hash === '#basket' && window.bootstrap) window.bootstrap.Offcanvas.getOrCreateInstance(drawer).show();
    window.addEventListener('storage', function (e) { if (e.key === CART_KEY) { render(); updateCartBadges(); } });
    render();
  }

  /* ----------------------------------------------------------------
   * Visit page: farm map, picking estimator, copy address, checklist
   * ---------------------------------------------------------------- */
  function initFarmMap() {
    var wrap = $('[data-farm-map]');
    if (!wrap) return;
    var buttons = $$('[data-area-btn]', wrap);
    var panel = $('[data-area-panel]', wrap);
    var areas = $$('[data-area]', wrap);

    var frame = $('.map-frame', wrap);
    function select(id, btn, scroll) {
      areas.forEach(function (a) { a.classList.toggle('is-active', a.getAttribute('data-area') === id); });
      // On small screens the map scrolls sideways: bring the chosen place into view.
      var area = areas.filter(function (a) { return a.getAttribute('data-area') === id; })[0];
      if (scroll && area && frame && frame.scrollWidth > frame.clientWidth) {
        var ab = area.getBoundingClientRect(), fb = frame.getBoundingClientRect();
        frame.scrollBy({ left: ab.left + ab.width / 2 - (fb.left + fb.width / 2), behavior: reduceMotion ? 'auto' : 'smooth' });
      }
      buttons.forEach(function (b) { b.setAttribute('aria-pressed', b === btn ? 'true' : 'false'); });
      if (panel && btn) {
        $('[data-panel-title]', panel).textContent = btn.getAttribute('data-title') || $('.area-name', btn).textContent;
        $('[data-panel-text]', panel).textContent = btn.getAttribute('data-desc');
        $('[data-panel-num]', panel).textContent = btn.getAttribute('data-num');
      }
    }
    buttons.forEach(function (b) {
      b.addEventListener('click', function () { select(b.getAttribute('data-area-btn'), b, true); });
    });
    areas.forEach(function (a) {
      a.addEventListener('click', function () {
        var btn = buttons.filter(function (b) { return b.getAttribute('data-area-btn') === a.getAttribute('data-area'); })[0];
        if (btn) { select(a.getAttribute('data-area'), btn); btn.focus(); }
      });
    });
    if (buttons[0]) select(buttons[0].getAttribute('data-area-btn'), buttons[0]);
  }

  function initPickEstimator() {
    var form = $('[data-estimator]');
    if (!form) return;
    var crop = $('select', form);
    var kg = $('input[type=range]', form);
    var kgOut = $('[data-kg-out]', form);
    var out = $('[data-estimate]', form);
    var note = $('[data-estimate-note]', form);
    function update() {
      var opt = crop.options[crop.selectedIndex];
      var price = parseFloat(opt.getAttribute('data-price'));
      var perKg = opt.getAttribute('data-unit') !== 'each';
      var amount = parseFloat(kg.value);
      kgOut.textContent = amount + (perKg ? ' kg' : (amount === 1 ? ' item' : ' items'));
      out.textContent = money.format(price * amount);
      note.textContent = opt.getAttribute('data-note') || '';
      kg.setAttribute('aria-valuetext', kgOut.textContent);
    }
    crop.addEventListener('change', function () {
      var opt = crop.options[crop.selectedIndex];
      var each = opt.getAttribute('data-unit') === 'each';
      kg.min = each ? 1 : 0.5; kg.max = each ? 20 : 10; kg.step = each ? 1 : 0.5;
      if (each) kg.value = Math.max(1, Math.round(kg.value));
      update();
    });
    kg.addEventListener('input', update);
    update();
  }

  function initCopyAddress() {
    $$('[data-copy]').forEach(function (btn) {
      btn.addEventListener('click', function () {
        var text = btn.getAttribute('data-copy');
        var label = $('.copy-label', btn);
        var done = function (ok) {
          if (label) label.textContent = ok ? 'Copied' : 'Press Ctrl+C to copy';
          setTimeout(function () { if (label) label.textContent = 'Copy address'; }, 2000);
        };
        if (navigator.clipboard && navigator.clipboard.writeText) {
          navigator.clipboard.writeText(text).then(function () { done(true); }, function () { done(false); });
        } else { done(false); }
      });
    });
  }

  function initChecklist() {
    var list = $('[data-checklist]');
    if (!list) return;
    var key = 'brookmoor-pick-list';
    var saved = store(key) || {};
    $$('input[type=checkbox]', list).forEach(function (box) {
      if (saved[box.value]) box.checked = true;
      box.addEventListener('change', function () { saved[box.value] = box.checked; store(key, saved); });
    });
  }

  /* ----------------------------------------------------------------
   * Boot
   * ---------------------------------------------------------------- */
  function init() {
    initNav();
    initStickyHeader();
    initScrollSpy();
    initOpenStatus();
    initWeekLabel();
    initSeason();
    initForms();
    initBoxBuilder();
    initBooking();
    initCounters();
    initReveal();
    initSmoothAnchors();
    initShopFilters();
    initCart();
    initPostcodeChecker();
    initFarmMap();
    initPickEstimator();
    initCopyAddress();
    initChecklist();
  }

  if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', init);
  else init();
})();
