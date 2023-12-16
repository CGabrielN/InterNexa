package socialnetwork.repository.paging;

import java.util.stream.Stream;

public interface Page <E>{

    Pageable getPageable();

    void nextPageable();

    void previousPageable();

    void resetPageable();

    Stream<E> getContent();
}
