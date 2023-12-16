package socialnetwork.repository.paging;

import java.util.stream.Stream;

public class PageImplementation <T> implements Page<T>{


    private Pageable pageable;

    private final Stream<T> content;

    public PageImplementation(Pageable pageable, Stream<T> content) {
        this.pageable = pageable;
        this.content = content;
    }

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

    @Override
    public void nextPageable() {
        this.pageable =  new PageableImplementation(this.pageable.pageNumber() + 1, this.pageable.pageSize());
    }

    @Override
    public void  previousPageable(){
        this.pageable = new PageableImplementation(this.pageable.pageNumber() - 1, this.pageable.pageSize());
    }

    @Override
    public void resetPageable() {
        int startPage = 1;
        this.pageable = new PageableImplementation(startPage, this.pageable.pageSize());
    }

    @Override
    public Stream<T> getContent() {
        return this.content;
    }
}
