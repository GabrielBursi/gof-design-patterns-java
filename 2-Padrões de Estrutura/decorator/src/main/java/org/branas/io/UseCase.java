package org.branas.io;

public interface UseCase<TInput, TOutput> {
  TOutput execute(TInput input);
}