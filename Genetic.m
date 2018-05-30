% To be clear.. this is not a path finding problem. This is the Travelling Salesman 
% problem (http://goo.gl/BsbW). In this problem a salesman starts at a given town
% and then has to visit a list of other towns (in any order). The aim is to find the
% best order in which to visit towns so that he minimizes travel distance.
% In Kwok's variation he ends in a specified town instead of returning home which is
% the traditional problem.

function mainGenetic()
    global start;
    global dest;
    global towns;

    % --------- START CONFIG ----------

    % Problem Parameters
    RANGE = [-55 55 -45 45];
    N_TOWNS = 15;
    % Genetic Algorithm Parameters
    POPULATION_SIZE = 200;
    N_GENERATIONS = 500;
    P_MUTATE = 0.1;
    P_CROSSOVER = 0.5;
    
    % --------- END CONFIG ----------
    
    % We assume population size is even. Guarantee this.
    POPULATION_SIZE = POPULATION_SIZE + mod(POPULATION_SIZE,2);
    
    % Generate random towns to visit
    points = rand(N_TOWNS+2,2);
    points = [points(:,1).*(RANGE(2)-RANGE(1))+RANGE(1), points(:,2).*(RANGE(4)-RANGE(3))+RANGE(3)];
    start = points(1,:);
    dest = points(end,:);
    towns = points(2:end-1,:);
    
    % Generate Population by randomly shuffling the towns
    population = zeros(POPULATION_SIZE, N_TOWNS);
    for i=1:POPULATION_SIZE
        population(i,:) = randperm(N_TOWNS);
    end
    
    % Create Plot
    figure(1); clf;
    plot(points(:,1), points(:,2), 'r*');
    labels = ['Start';cellstr( num2str([1:N_TOWNS]') );'End'];
    text(points(:,1), points(:,2), labels, 'VerticalAlignment','bottom', 'HorizontalAlignment','right');
    axis([-60 60 -50 50]);
    pathH = [];

    % Simulation Loop
    for generation=1:N_GENERATIONS
        % Evaluate Fitness (better should be larger)
        perf = performance(population);
        fitness = (max(perf)-perf).^2;
        
        % Cumulative fitness is normalised so we have a distribution 
        % to help select the intermediate population.
        cum_fit = cumsum(fitness);
        cum_fit = cum_fit/max(cum_fit);

        % Selection
        intermediate_population = zeros(POPULATION_SIZE, N_TOWNS);
        for j=1:POPULATION_SIZE
            intermediate_population(j,:) = select_intermediate(population, cum_fit);
        end
        
        % Recombination / Crossover
            % Note: We are performing crossover on an Ordered Chromosome. This means
            %       we will likely produce errors that need to be repaired. There are
            %       alternate methods to avoid this: http://goo.gl/Wql0OL
        new_population = zeros(POPULATION_SIZE, N_TOWNS);
        for j=2:2:POPULATION_SIZE
            % Select two parents
            parent_1 = intermediate_population(j-1,:);
            parent_2 = intermediate_population(j,:);
            % With probability P_CROSSOVER
            if rand() < P_CROSSOVER
                % Perform 2-Point crossover. I have chosen this over one point crossover
                %   as it has a better chance of unwinding loops and swapping sections
                %   given the nature of the problem and chromosome encoding.
                cp = sort(randi(N_TOWNS-1,2,1)); % Crossover Points
                child_1 = [parent_1(1:cp(1)) parent_2(cp(1)+1:cp(2)) parent_1(cp(2)+1:end)];
                child_2 = [parent_2(1:cp(1)) parent_1(cp(1)+1:cp(2)) parent_2(cp(2)+1:end)];
                % Repair the faulty children and push them into the new population
                new_population(j-1,:) = repair(child_1);
                new_population(j,:) = repair(child_2);
            else
                % Carry parents over to new population without crossover
                new_population(j-1,:) = parent_1;
                new_population(j,:) = parent_2;
            end
        end
        
        % Mutation
        for j=2:POPULATION_SIZE
            % For each parameter in the chromosome string...
            for i=1:N_TOWNS
                % ...with probability P_MUTATE...
                if rand() < P_MUTATE
                    % ...pick another random parameter and swap them
                    k = randi(N_TOWNS,1);
                    new_population(j,[i k]) = new_population(j,[k i]);
                end
            end
        end
        
        % Elitism - copy best parent to new population
        best = population(find(perf == min(perf),1,'first'),:);
        new_population(1,:) = best;
        population = new_population;
        
        % Update Plot
        if (ishandle(pathH))
            delete(pathH);
        end
        hold on;
        path = [start; towns(best,:); dest];
        pathH = plot(path(:,1),path(:,2), 'b-');
        title(sprintf('Generation %d', generation));
        drawnow;
    end
    display(sprintf('Best path: %s\n', sprintf('%d ', best)));
end

% Repair a faulty chromosome
function c = repair(c)
    n = length(c);
    % Create a list of missing towns
    missing = setdiff(1:n,c);
    for i=randperm(length(c)) %Go through in random order
        % If there is duplicate town, replace it with a missing town
        if length(find(c == c(i))) > 1
            c(i) = missing(end);
            missing = missing(1:end-1);
        end
    end
end

% Select a chromosome for the intermediate population
%   "Roulette Wheel" method: Uses the cumulative fitness function to increase the
%   probability of chromosomes with higher fitness being included.
function c = select_intermediate(population, cum_fit)
    if isnan(cum_fit(1)) % No fitness when all chromosones equal. Pick randomly.
        c = population(randi(length(cum_fit),1),:);
    else
        i = find(cum_fit >= rand(),1,'first');
        c = population(i,:);
    end
end

% Calculate the performance of each chromosome.
%   "Performance" is considered the total distance travelled. So a lower performance is
%   better. Fitness is calculated using this such that higher fitness is better.
function sum = performance(chromosome)
    global start;
    global dest;
    global towns;
    sum = zeros(size(chromosome,1),1);
    for j=1:size(chromosome,1)
        sum(j) = dist(start, towns(chromosome(j,1),:)) + dist(towns(chromosome(j,end),:),dest);
        for i=2:size(chromosome,2)
            sum(j) = sum(j) + dist(towns(chromosome(j,i-1),:), towns(chromosome(j,i),:));
        end
    end
    
    % Internal function - pythagorean distance.
    function d = dist(A, B)
        d = sqrt((A(1)-B(1))^2 + (A(2)-B(2))^2);
    end
end
