package com.csen_359.design_patterns.iterator;

import com.csen_359.design_patterns.domain.UsageEntry;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Iterator pattern - pages through a user's usage history without loading the
 * entire result set into memory.
 *
 * <p>Each call to {@code next()} fetches one page from the database. Callers
 * process entries page-by-page using a standard {@code while (it.hasNext())}
 * loop without knowing how pagination or sorting are implemented.
 *
 * <p>Must be used within an active Spring transaction when the underlying
 * repository cursor requires one (e.g., for streaming).
 */
public class UsagePageIterator implements Iterator<List<UsageEntry>> {

    private final UsageEntryRepository repository;
    private final long userId;
    private final LocalDateTime from;
    private final LocalDateTime to;
    private final int pageSize;

    private int currentPage = 0;
    private List<UsageEntry> prefetched;

    public UsagePageIterator(UsageEntryRepository repository, long userId,
                             LocalDateTime from, LocalDateTime to, int pageSize) {
        this.repository  = repository;
        this.userId      = userId;
        this.from        = from;
        this.to          = to;
        this.pageSize    = pageSize;
    }

    @Override
    public boolean hasNext() {
        if (prefetched == null) {
            prefetched = fetchPage(currentPage);
        }
        return !prefetched.isEmpty();
    }

    @Override
    public List<UsageEntry> next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more usage pages");
        }
        List<UsageEntry> page = prefetched;
        prefetched = null;
        currentPage++;
        return page;
    }

    private List<UsageEntry> fetchPage(int page) {
        return repository.findByUserIdAndLoggedAtBetween(
                userId, from, to,
                PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "loggedAt")));
    }
}
