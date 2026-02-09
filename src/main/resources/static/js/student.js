let allStudents = [];
let openEditId = null;
let searchTimeout = null;

/* ---------- Toast Notifications ---------- */
function showToast(message, type = 'info') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type} show`;
    setTimeout(() => toast.classList.remove('show'), 3000);
}

/* ---------- ADD STUDENT ---------- */
function addStudent(e) {
    e?.preventDefault();
    const nameEl = document.getElementById('name');
    const ageEl = document.getElementById('age');
    const emailEl = document.getElementById('email');

    const student = {
        name: nameEl.value.trim(),
        age: Number(ageEl.value),
        email: emailEl.value.trim()
    };

    fetch('/students', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(student)
    })
        .then(res => {
            if (!res.ok) return res.json().then(err => { throw err; });
            return res.json();
        })
        .then(() => {
            clearForm();
            loadStudents();
            showToast('Student added successfully!', 'success');
        })
        .catch(err => {
            const msg = err?.errors ? Object.values(err.errors).join(' ') : err?.message || 'Failed to add student';
            showToast(msg, 'error');
        });
}

function clearForm() {
    document.getElementById('name').value = '';
    document.getElementById('age').value = '';
    document.getElementById('email').value = '';
}

/* ---------- LOAD STUDENTS ---------- */
function loadStudents() {
    const search = document.getElementById('searchInput')?.value?.trim();
    const url = search ? `/students?search=${encodeURIComponent(search)}` : '/students';

    fetch(url)
        .then(res => res.json())
        .then(data => {
            allStudents = data;
            applySortFilter();
        })
        .catch(() => showToast('Failed to load students', 'error'));
}

function handleSearch() {
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(() => {
        loadStudents();
    }, 300);
}

/* ---------- RENDER TABLE ---------- */
function renderStudents(students) {
    const table = document.getElementById('studentTable');
    const emptyState = document.getElementById('emptyState');
    const totalCount = document.getElementById('totalCount');
    const filteredCount = document.getElementById('filteredCount');

    if (!table) return;

    const total = allStudents.length;
    totalCount.textContent = total;
    filteredCount.textContent = students.length;

    if (students.length === 0) {
        table.innerHTML = '';
        emptyState?.classList.add('visible');
        return;
    }

    emptyState?.classList.remove('visible');

    table.innerHTML = students.map((s, idx) => `
        <tr>
            <td>${idx + 1}</td>
            <td>${escapeHtml(s.name)}</td>
            <td>${s.age}</td>
            <td>${escapeHtml(s.email)}</td>
            <td>
                <div class="action-btns">
                    <button class="btn btn-edit" onclick="toggleEdit('${s.id}')">Edit</button>
                    <button class="btn btn-delete" onclick="deleteStudent('${s.id}')">Delete</button>
                </div>
            </td>
        </tr>
        <tr id="edit-${s.id}" style="display:none" class="edit-row">
            <td colspan="5">
                <div class="edit-box">
                    <input type="text" id="name-${s.id}" value="${escapeHtml(s.name)}" placeholder="Name">
                    <input type="number" id="age-${s.id}" value="${s.age}" min="5" max="90" placeholder="Age">
                    <input type="email" id="email-${s.id}" value="${escapeHtml(s.email)}" placeholder="Email">
                    <button class="btn btn-update" onclick="updateStudent('${s.id}')">Update</button>
                    <button class="btn btn-cancel" onclick="closeEdit('${s.id}')">Cancel</button>
                </div>
            </td>
        </tr>
    `).join('');
}

function escapeHtml(str) {
    if (!str) return '';
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}

/* ---------- SORT & FILTER ---------- */
function applySortFilter() {
    const sortSelect = document.getElementById('sortSelect');
    const ageFilter = document.getElementById('ageFilter');

    let result = [...allStudents];

    const ageRange = ageFilter?.value;
    if (ageRange) {
        const [min, max] = ageRange.split('-').map(Number);
        result = result.filter(s => s.age >= min && s.age <= max);
    }

    const sortVal = sortSelect?.value;
    if (sortVal) {
        const [field, dir] = sortVal.split('-');
        result.sort((a, b) => {
            let va = a[field];
            let vb = b[field];
            if (typeof va === 'string') {
                va = va.toLowerCase();
                vb = (vb || '').toLowerCase();
            }
            const cmp = va < vb ? -1 : va > vb ? 1 : 0;
            return dir === 'asc' ? cmp : -cmp;
        });
    }

    renderStudents(result);
}

/* ---------- EDIT ---------- */
function toggleEdit(id) {
    if (openEditId && openEditId !== id) {
        const prev = document.getElementById(`edit-${openEditId}`);
        if (prev) prev.style.display = 'none';
    }

    const row = document.getElementById(`edit-${id}`);
    if (!row) return;

    row.style.display = row.style.display === 'none' ? 'table-row' : 'none';
    openEditId = row.style.display === 'table-row' ? id : null;
}

function closeEdit(id) {
    const row = document.getElementById(`edit-${id}`);
    if (row) row.style.display = 'none';
    openEditId = null;
}

function updateStudent(id) {
    const student = {
        name: document.getElementById(`name-${id}`).value.trim(),
        age: Number(document.getElementById(`age-${id}`).value),
        email: document.getElementById(`email-${id}`).value.trim()
    };

    fetch(`/students/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(student)
    })
        .then(res => {
            if (!res.ok) return res.json().then(err => { throw err; });
            return res.json();
        })
        .then(() => {
            closeEdit(id);
            loadStudents();
            showToast('Student updated!', 'success');
        })
        .catch(err => {
            const msg = err?.errors ? Object.values(err.errors).join(' ') : err?.message || 'Update failed';
            showToast(msg, 'error');
        });
}

/* ---------- DELETE ---------- */
function deleteStudent(id) {
    if (!confirm('Delete this student?')) return;

    fetch(`/students/${id}`, { method: 'DELETE' })
        .then(res => {
            if (!res.ok) throw new Error('Delete failed');
            closeEdit(id);
            loadStudents();
            showToast('Student deleted', 'info');
        })
        .catch(() => showToast('Failed to delete student', 'error'));
}

window.addEventListener('DOMContentLoaded', loadStudents);
