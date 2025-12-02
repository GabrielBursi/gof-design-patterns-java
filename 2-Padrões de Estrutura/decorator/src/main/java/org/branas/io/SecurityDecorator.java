package org.branas.io;

import org.jetbrains.annotations.NotNull;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SecurityDecorator<TInput, TOutput> implements UseCase<TInput, TOutput> {
    private final UseCase<TInput, TOutput> useCase;

    @Override
    public TOutput execute(@NotNull TInput input) {
        System.out.println("security");
        return useCase.execute(input);
    }
}
