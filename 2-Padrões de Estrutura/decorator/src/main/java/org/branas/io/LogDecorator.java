package org.branas.io;

import org.jetbrains.annotations.NotNull;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogDecorator<TInput, TOutput> implements UseCase<TInput, TOutput> {
    private final UseCase<TInput, TOutput> useCase;

    @Override
    public TOutput execute(@NotNull TInput input) {
        System.out.println("log: " + input);
        return useCase.execute(input);
    }
}
